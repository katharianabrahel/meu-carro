package com.example.meucarro.services.http;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000@\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J\u001e\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\t0\b0\u00032\b\b\u0001\u0010\n\u001a\u00020\u000bH\'J\u0018\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\u00032\b\b\u0001\u0010\f\u001a\u00020\u000eH\'J\u001e\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u00032\u000e\b\u0001\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\bH\'\u00a8\u0006\u0013"}, d2 = {"Lcom/example/meucarro/services/http/ApiService;", "", "createUser", "Lretrofit2/Call;", "Lcom/example/meucarro/services/http/models/UserResponse;", "user", "Lcom/example/meucarro/services/http/models/UserRequest;", "getSyncMaintence", "", "Lcom/example/meucarro/services/http/models/MaintenceResponse;", "id", "", "login", "Lcom/example/meucarro/services/http/models/LoginResponse;", "Lcom/example/meucarro/services/http/models/LoginResquest;", "syncMaintence", "Lcom/example/meucarro/services/http/models/SyncMaintenceResponse;", "sync", "Lcom/example/meucarro/services/http/models/SyncMaintenceRequest;", "app_debug"})
public abstract interface ApiService {
    
    @retrofit2.http.POST(value = "auth/login")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<com.example.meucarro.services.http.models.LoginResponse> login(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.meucarro.services.http.models.LoginResquest login);
    
    @retrofit2.http.POST(value = "users")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<com.example.meucarro.services.http.models.UserResponse> createUser(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    com.example.meucarro.services.http.models.UserRequest user);
    
    @retrofit2.http.POST(value = "maintences/sync")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<com.example.meucarro.services.http.models.SyncMaintenceResponse> syncMaintence(@retrofit2.http.Body()
    @org.jetbrains.annotations.NotNull()
    java.util.List<com.example.meucarro.services.http.models.SyncMaintenceRequest> sync);
    
    @retrofit2.http.GET(value = "maintences/sync")
    @org.jetbrains.annotations.NotNull()
    public abstract retrofit2.Call<java.util.List<com.example.meucarro.services.http.models.MaintenceResponse>> getSyncMaintence(@retrofit2.http.Query(value = "id")
    @org.jetbrains.annotations.NotNull()
    java.lang.String id);
}