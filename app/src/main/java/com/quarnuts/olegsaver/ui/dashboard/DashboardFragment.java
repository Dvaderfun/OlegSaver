package com.quarnuts.olegsaver.ui.dashboard;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import alirezat775.lib.kesho.Kesho;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.quarnuts.olegsaver.MainActivity;
import com.quarnuts.olegsaver.R;
import com.quarnuts.olegsaver.api.model.GetHospitalInfo;
import com.quarnuts.olegsaver.api.model.Hospital;
import com.quarnuts.olegsaver.api.retrofit.UserClient;
import com.quarnuts.olegsaver.blockchain.BloqlyClient;
import com.quarnuts.olegsaver.blockchain.KeyPair;
import com.quarnuts.olegsaver.blockchain.transaction.SignedTransaction;
import com.quarnuts.olegsaver.blockchain.transaction.Transaction;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.quarnuts.olegsaver.api.Api.BASE_URL;
import static com.quarnuts.olegsaver.api.Api.BLOCKCHAIN_URL;

public class DashboardFragment extends Fragment {

    private UserClient client;

    Kesho kesho;

    String hospitalTitle;
    String patientId;
    TextView hospital;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        kesho = new Kesho(getContext(), Kesho.SHARED_PREFERENCES_MANAGER, Kesho.Encrypt.NONE_ENCRYPT, "_hello_world_secret_key_");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        client = retrofit.create(UserClient.class);

        TextInputEditText hardness = root.findViewById(R.id.hardness);
        MaterialButton createAct = root.findViewById(R.id.button2);
        hospital = root.findViewById(R.id.hospital);
        hospital.setText("");

        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Surgeon");
        spinnerArray.add("Reanimation");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = root.findViewById(R.id.faculty_spinner);
        sItems.setAdapter(adapter);


        createAct.setOnClickListener(view -> {
            String selected = sItems.getSelectedItem().toString();
            getOptimalHospital(kesho.pull("token", ""), ((MainActivity) getActivity()).getLongitude(), ((MainActivity) getActivity()).getLatitude(), selected);

            //TODO selected
            new BlockchainTransaction(hospitalTitle, "patient1488", hardness.getText().toString(), selected).execute();
        });

        return root;
    }

    /**
     * @param token
     * @param myLongitude
     * @param myLatitude
     * @param title       - профессия врача (сложность проблемы) Reanimation, Surgeon
     */
    public void getOptimalHospital(String token, String myLongitude, String myLatitude, String title) {
        GetHospitalInfo getHospitalInfo = new GetHospitalInfo(token, myLatitude, myLongitude, title);
        Call<Hospital> c = client.getOptimalHospital(getHospitalInfo);

        c.enqueue(new Callback<Hospital>() {
            @Override
            public void onResponse(Call<Hospital> call, Response<Hospital> response) {
                Log.d("HOSPITAL", response.body().getId() +
                        response.body().getLatitude() +
                        response.body().getLongitude() +
                        response.body().getTitle());

                hospital.setText("Лікарня \n"+response.body().getTitle());
                hospitalTitle = response.body().getTitle() + " id: " + response.body().getId();
                new AlertDialog.Builder(getContext())
                        .setTitle("Обрано " + response.body().getTitle())
                        .setMessage("Найрелевантніший варіант із: 5. " + response.body().getLatitude() + " " + response.body().getLongitude())

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setIcon(R.drawable.ic_add_alert_black_24dp)
                        .show();
            }

            @Override
            public void onFailure(Call<Hospital> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to get response", Toast.LENGTH_LONG).show();
            }
        });
    }

    private class BlockchainTransaction extends AsyncTask<Void, String, String> {
        String hospName;
        String patientId;
        String hardness;
        String choice;

        public BlockchainTransaction(String hospitalName, String patientId, String hardness, String choice) {
            this.hardness = hardness;
            this.patientId = patientId;
            this.hospName = hospitalName;
            this.choice = choice;
        }

        // onPreExecute called before the doInBackgroud start for display
        // progress dialog.
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            String privateKey = "x9oZ6jrUVEBDP5C0005fPseqPwLshQbb9io7Upg8sNM=";
            String space = "space1";

            //Ключ - id больного
            String key = patientId;

            BloqlyClient bloqlyClient = new BloqlyClient(BLOCKCHAIN_URL);

            Optional<SignedTransaction> txOpt = bloqlyClient.getLastTransaction(space, key);

            Long nonce = txOpt.map(tx -> tx.getNonce() + 1).orElse(1L);

            KeyPair keyPair = KeyPair.fromPrivateKeyEncoded(privateKey);

            Transaction tx = new Transaction();

            tx.setSpace(space);
            tx.setKey(key);
            tx.setNonce(nonce);
            tx.setMemo(choice);
            tx.setTimestamp(Instant.now().toEpochMilli());
            tx.setValue("Hospital: " + hospName + "| Problem: " + hardness);

            List<String> tags = new ArrayList<>();
            tags.add("tag");
            tx.setTags(tags);

            SignedTransaction signedTx = keyPair.signTransaction(tx);

            bloqlyClient.submitTransaction(signedTx);
            return "ban";
        }

        // onPostExecute displays the results of the doInBackgroud and also we
        // can hide progress dialog.
        @Override
        protected void onPostExecute(String result) {
        }


    }
}