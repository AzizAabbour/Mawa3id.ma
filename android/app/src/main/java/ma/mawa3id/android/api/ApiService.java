package ma.mawa3id.android.api;

import ma.mawa3id.android.model.AuthResponse;
import ma.mawa3id.android.model.DashboardStats;
import ma.mawa3id.android.model.LoginRequest;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

public interface ApiService {

    @POST("api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @GET("api/dashboard/statistics")
    Call<DashboardStats> getDashboardStats();

    @GET("api/appointments")
    Call<Map<String, Object>> getAppointments();

    @PATCH("api/appointments/{id}/status")
    Call<Void> updateAppointmentStatus(@Path("id") Long id, @Body Map<String, String> statusMap);
}
