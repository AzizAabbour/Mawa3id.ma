package ma.mawa3id.android.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ma.mawa3id.android.R;
import ma.mawa3id.android.api.ApiClient;
import ma.mawa3id.android.model.DashboardStats;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView txtWelcome, txtBusinessName, txtTodayCount, txtRevenue, txtWhatsappCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtWelcome = findViewById(R.id.txtWelcome);
        txtBusinessName = findViewById(R.id.txtBusinessName);
        txtTodayCount = findViewById(R.id.txtTodayCount);
        txtRevenue = findViewById(R.id.txtRevenue);
        txtWhatsappCount = findViewById(R.id.txtWhatsappCount);

        SharedPreferences prefs = getSharedPreferences("mawa3id_prefs", Context.MODE_PRIVATE);
        String userName = prefs.getString("user_name", "Aziz");
        String businessName = prefs.getString("business_name", "Salon Riad Beauty");

        txtWelcome.setText("Bonjour " + userName + " 👋");
        txtBusinessName.setText(businessName);

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            prefs.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        loadStats();
    }

    private void loadStats() {
        ApiClient.getService(this).getDashboardStats().enqueue(new Callback<DashboardStats>() {
            @Override
            public void onResponse(Call<DashboardStats> call, Response<DashboardStats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DashboardStats stats = response.body();
                    txtTodayCount.setText(String.valueOf(stats.getTodayAppointmentsCount()));
                    txtRevenue.setText((stats.getMonthlyEstimatedRevenueMad() != null ? stats.getMonthlyEstimatedRevenueMad() : "0") + " DH");
                    txtWhatsappCount.setText(String.valueOf(stats.getWhatsappSentCount()));
                }
            }

            @Override
            public void onFailure(Call<DashboardStats> call, Throwable t) {
                // Keep default placeholder values
            }
        });
    }
}
