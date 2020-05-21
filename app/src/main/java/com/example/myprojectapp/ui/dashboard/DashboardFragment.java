package com.example.myprojectapp.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myprojectapp.R;
import com.example.myprojectapp.SecondActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        new JsonTask().execute("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?login=a19klafr");
        return root;
    }
    @SuppressLint("StaticFieldLeak")
    private class JsonTask extends AsyncTask<String, String, String> {

        private HttpURLConnection connection = null;
        private BufferedReader reader = null;

        private ArrayList<String> riverNames=new ArrayList<String>();
        private ArrayList<String> riverLocation=new ArrayList<String>();
        private ArrayList<Integer> riverLength=new ArrayList<Integer>();
        private ArrayList<String> riverImg=new ArrayList<String>();


        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null && !isCancelled()) {
                    builder.append(line).append("\n");
                }
                return builder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            Log.d("TAG", json);
            try {
                JSONArray jsonArray = new JSONArray(json);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    String location = jsonObject.getString("location");
                    int length = jsonObject.getInt("size");
                    String image = jsonObject.getString("auxdata");

                    riverNames.add(name);
                    riverLocation.add(location);
                    riverLength.add(length);
                    riverImg.add(image);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getView().getContext(), R.layout.list_items, R.id.listview_items, riverNames);
                ListView riverList = (ListView)getActivity().findViewById(R.id.river_list_view);
                riverList.setAdapter(adapter);
                riverList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                            Log.d("MSG", riverNames.get(i) + ", " + riverLocation.get(i)
                                    + ", " + riverLength.get(i));

                            Intent intent = new Intent(getActivity(), SecondActivity.class);
                            intent.putExtra("name", riverNames.get(i));
                            intent.putExtra("location", riverLocation.get(i));
                            intent.putExtra("length", riverLength.get(i));
                            intent.putExtra("image", riverImg.get(i));
                            startActivity(intent);
                        }
                });

            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
