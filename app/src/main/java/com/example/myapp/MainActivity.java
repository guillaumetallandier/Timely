package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class MainActivity extends AppCompatActivity {

    private TextView txtv;
    private EditText etxt;


    private RecyclerView recyclerView;
    private List<Bus> mesBus;
    private MyBusAdapter monAdapter;

   /* public static final String SHARED_PREFS =" sharedPrefs";
    public static final String TEXT="text";
    private String textShared;
  */
  ;

   private SharedPreferences mPreferences;
   private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("patate 2");

        setContentView(R.layout.activity_main);
        Button btnfav = findViewById(R.id.btnajoutFav);
        final TextView txtv =findViewById(R.id.txtv1);
        Button btn = findViewById(R.id.btn1);
        etxt = findViewById(R.id.etxt1);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        System.out.println("patate 1");
        checkedSharedPreferences();
        System.out.println("patate 2");

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new geturl(txtv).execute();
            }
        });
        System.out.println("patate 3");


        btnfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // saveData();
                String name= etxt.getText().toString();
                mEditor.putString(getString(R.string.EditTextStop),name);
                mEditor.apply();
            }
        });


        System.out.println("patate 4");
      //  loadData();
    }
    public void checkedSharedPreferences(){
        String nameStop=mPreferences.getString(getString(R.string.EditTextStop),"");
        etxt.setText(nameStop);
    }
/*

    public void saveData(){
        SharedPreferences sharedPreferences =getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT,etxt.getText().toString());
        editor.apply();

        Toast.makeText(this,"Data Saved",Toast.LENGTH_SHORT).show();
    };


    public void loadData(){
        SharedPreferences sharedPreferences =getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        textShared=sharedPreferences.getString(TEXT,"");
        etxt.setText(textShared);
    }

*/

    private class geturl extends AsyncTask<Void, Void, String> {
        private TextView txtv;
        private String urlString = "https://api.cts-strasbourg.eu/v1/siri/2.0/stoppoints-discovery?RequestorRef=d15b2824-e939-4951-bb65-b2038f14c9ea";


        public geturl(TextView txtv){
            this.txtv=txtv;
        }


        @Override
        protected String doInBackground(Void... params) {
            String number = "";
            HttpURLConnection urlConnection = null;
            JSONObject reponse = null;

            try {
                URL myUrl = new URL(urlString);
                urlConnection = (HttpURLConnection) myUrl.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic ZDE1YjI4MjQtZTkzOS00OTUxLWJiNjUtYjIwMzhmMTRjOWVhOg==");
                urlConnection.setRequestMethod("GET");
                Scanner scanner = new Scanner(urlConnection.getInputStream()).useDelimiter("\\A");
                reponse = new JSONObject(scanner.next());

                System.out.println("patate1");
                JSONArray jsonArray = reponse.getJSONObject("StopPointsDelivery").getJSONArray("AnnotatedStopPointRef");
                System.out.println("patate2");
                System.out.println(jsonArray.length());
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject arrest = jsonArray.getJSONObject(i);
                    if (arrest.getString("StopName").equals(etxt.getText().toString())) {
                        System.out.println("michel");
                        number = arrest.getJSONObject("Extension").getString("StopCode");
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("numéro arret :"+number);
            return number;
        }

        @Override
        protected void onPostExecute(String number) {
            System.out.println(number);
            if (number.length() != 0) {
                new gethoraire(this.txtv).execute(number);
            }
        }


    }

    private class gethoraire extends AsyncTask<String, Void, JSONObject> {

        private TextView txtv;

        public gethoraire(TextView txtv){
         this.txtv=txtv;
        }

        @Override
        protected JSONObject doInBackground(String... number) {
            txtv=findViewById(R.id.txtv1);
            String stopNumber = number[0].substring(0, (number[0].length() - 1));
            System.out.println((stopNumber));
            String url2 = "https://api.cts-strasbourg.eu/v1/siri/2.0/stop-monitoring?MonitoringRef=" + stopNumber + "&RequestorRef=d15b2824-e939-4951-bb65-b2038f14c9ea";
            HttpURLConnection urlConnection = null;
            JSONObject reponse = null;
            try {
                URL myUrl = new URL(url2);
                urlConnection = (HttpURLConnection) myUrl.openConnection();
                urlConnection.setRequestProperty("Authorization", "Basic ZDE1YjI4MjQtZTkzOS00OTUxLWJiNjUtYjIwMzhmMTRjOWVhOg==");
                urlConnection.setRequestMethod("GET");
                Scanner scanner = new Scanner(urlConnection.getInputStream()).useDelimiter("\\A");
                reponse = new JSONObject(scanner.next());
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
            return reponse;
        }

        @Override
        protected void onPostExecute(JSONObject reponse) {

            recyclerView=(RecyclerView)findViewById(R.id.myRyclerViewBus);
            mesBus=new ArrayList<>();

            this.txtv=findViewById(R.id.txtv1);
            this.txtv.setText("");
            System.out.println("patate5");
            System.out.println(reponse.toString());
            JSONArray jsonArray = null;
            try {

                jsonArray = reponse.getJSONObject("ServiceDelivery").getJSONArray("StopMonitoringDelivery").getJSONObject(0).getJSONArray("MonitoredStopVisit");
                System.out.println("patate6");
                System.out.println(jsonArray.length());


                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject arrest = jsonArray.getJSONObject(i);
                    String line = arrest.getJSONObject("MonitoredVehicleJourney").getString("LineRef");
                    String direction = arrest.getJSONObject("MonitoredVehicleJourney").getString("DestinationName");
                    String horaire = arrest.getJSONObject("MonitoredVehicleJourney").getJSONObject("MonitoredCall").getString("ExpectedDepartureTime");
                    System.out.println("line : " + line);
                    System.out.println("direction :" + direction);
                    System.out.println("horaire :" + horaire);
                    mesBus.add(new Bus(horaire.substring(11,16),line,direction));
                 //   this.txtv.append(line + ", " + direction + " : " + horaire + "\n\n\n\n");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            System.out.println(mesBus.toString());
            monAdapter=new MyBusAdapter(mesBus);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
            recyclerView.setAdapter(monAdapter);
        }
    }

    class MyBusAdapter extends RecyclerView.Adapter<MyBusAdapter.MyViewHolder>{
       private List<Bus> mesBus;
        class MyViewHolder extends RecyclerView.ViewHolder{

            private TextView txtVHoraire;
            private TextView txtVDirection;
            private TextView txtVLigne;
            private ImageView imgVLogo;


            MyViewHolder(View itemView){
                super(itemView);
                txtVDirection=(TextView) itemView.findViewById(R.id.txtVDirection);
                txtVHoraire=(TextView) itemView.findViewById(R.id.txtVHoraire);
                imgVLogo=(ImageView)  itemView.findViewById(R.id.imgVlogo);
            }

            void display(Bus bus){
                txtVHoraire.setText(bus.getHoraire());
                txtVDirection.setText(bus.getDirection());
                switch(bus.getLigne()){
                    case "A":
                        imgVLogo.setImageResource(R.drawable.trama);
                        break;
                    case "B":
                        imgVLogo.setImageResource(R.drawable.tramb);
                        break;
                    case "C":
                        imgVLogo.setImageResource(R.drawable.tramc);
                        break;
                    case "D":
                        imgVLogo.setImageResource(R.drawable.tramd);
                        break;
                    case "E":
                        imgVLogo.setImageResource(R.drawable.trame);
                        break;
                    case "2":
                        imgVLogo.setImageResource(R.drawable.ligne2);
                        break;
                    case "4":
                        imgVLogo.setImageResource(R.drawable.ligne4);
                        break;
                    case "4A":
                        imgVLogo.setImageResource(R.drawable.ligne4a);
                        break;
                    case "6":
                        imgVLogo.setImageResource(R.drawable.ligne6);
                        break;
                    case "6A":
                        imgVLogo.setImageResource(R.drawable.ligne6a);
                        break;
                    case "6B":
                        imgVLogo.setImageResource(R.drawable.ligne6b);
                        break;
                    case "10":
                        imgVLogo.setImageResource(R.drawable.ligne10);
                        break;
                    case "12":
                        imgVLogo.setImageResource(R.drawable.ligne12);
                        break;
                    case "13":
                        imgVLogo.setImageResource(R.drawable.ligne13);
                        break;
                    case "14":
                        imgVLogo.setImageResource(R.drawable.ligne14);
                        break;
                    case "15":
                        imgVLogo.setImageResource(R.drawable.ligne15);
                        break;
                    case "15A":
                        imgVLogo.setImageResource(R.drawable.ligne15a);
                        break;
                    case "17":
                        imgVLogo.setImageResource(R.drawable.ligne17);
                        break;
                    case "19":
                        imgVLogo.setImageResource(R.drawable.ligne19);
                        break;
                    case "21":
                        imgVLogo.setImageResource(R.drawable.ligne21);
                        break;
                    case "22":
                        imgVLogo.setImageResource(R.drawable.ligne22);
                        break;
                    case "24":
                        imgVLogo.setImageResource(R.drawable.ligne24);
                        break;
                    case "27":
                        imgVLogo.setImageResource(R.drawable.ligne27);
                        break;
                    case "29":
                        imgVLogo.setImageResource(R.drawable.ligne29);
                        break;
                    case "30":
                        imgVLogo.setImageResource(R.drawable.ligne30);
                        break;
                    case "31":
                        imgVLogo.setImageResource(R.drawable.ligne31);
                        break;
                    case "40":
                        imgVLogo.setImageResource(R.drawable.ligne40);
                        break;
                    case "41":
                        imgVLogo.setImageResource(R.drawable.ligne41);
                        break;
                    case "42":
                        imgVLogo.setImageResource(R.drawable.ligne42);
                        break;
                    case "43":
                        imgVLogo.setImageResource(R.drawable.ligne43);
                        break;
                    case "44":
                        imgVLogo.setImageResource(R.drawable.ligne44);
                        break;
                    case "45":
                        imgVLogo.setImageResource(R.drawable.ligne45);
                        break;
                    case "50":
                        imgVLogo.setImageResource(R.drawable.ligne50);
                        break;
                    case "50A":
                        imgVLogo.setImageResource(R.drawable.ligne50a);
                        break;
                    case "57":
                        imgVLogo.setImageResource(R.drawable.ligne57);
                        break;
                    case "60":
                        imgVLogo.setImageResource(R.drawable.ligne60);
                        break;
                    case "62":
                        imgVLogo.setImageResource(R.drawable.ligne62);
                        break;
                    case "63":
                        imgVLogo.setImageResource(R.drawable.ligne63);
                        break;
                    case "64":
                        imgVLogo.setImageResource(R.drawable.ligne64);
                        break;
                    case "67":
                        imgVLogo.setImageResource(R.drawable.ligne67);
                        break;
                    case "70":
                        imgVLogo.setImageResource(R.drawable.ligne70);
                        break;
                    case "71":
                        imgVLogo.setImageResource(R.drawable.ligne71);
                        break;
                    case "71A":
                        imgVLogo.setImageResource(R.drawable.ligne71a);
                        break;
                    case "72":
                        imgVLogo.setImageResource(R.drawable.ligne72);
                        break;
                    case "72A":
                        imgVLogo.setImageResource(R.drawable.ligne72a);
                        break;
                    case "73":
                        imgVLogo.setImageResource(R.drawable.ligne73);
                        break;
                    case "74":
                        imgVLogo.setImageResource(R.drawable.ligne74);
                        break;
                    case "75":
                        imgVLogo.setImageResource(R.drawable.ligne75);
                        break;
                    case "76":
                        imgVLogo.setImageResource(R.drawable.ligne76);
                        break;
                    case "G":
                        imgVLogo.setImageResource(R.drawable.ligneg);
                        break;
                    case "H":
                        imgVLogo.setImageResource(R.drawable.ligneh);
                        break;
                    case "L1":
                        imgVLogo.setImageResource(R.drawable.lignel1);
                        break;
                    case "L3":
                        imgVLogo.setImageResource(R.drawable.lignel3);
                        break;
                    case "L6":
                        imgVLogo.setImageResource(R.drawable.lignel6);
                        break;
                    default:
                        imgVLogo.setImageResource(R.drawable.trame);
                }
            }
        }

        public MyBusAdapter(List<Bus> mesbus){
            this.mesBus=mesbus;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
            LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.affiche_bus,parent,false);
            return new MyViewHolder(view);
        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position){
            holder.display(mesBus.get(position));
        }

        @Override
        public int getItemCount(){
            return mesBus.size();
        }
    }


}