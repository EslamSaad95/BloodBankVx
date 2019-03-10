package com.example.laptophome.bank_eldam.data.rest;

import com.example.laptophome.bank_eldam.data.model.adddonationrequest.AddDonationRequest;
import com.example.laptophome.bank_eldam.data.model.bloodtypelist.BloodtypeList;
import com.example.laptophome.bank_eldam.data.model.cities.Cities;
import com.example.laptophome.bank_eldam.data.model.contact.Contacts;
import com.example.laptophome.bank_eldam.data.model.donationdetails.DonationDetails;
import com.example.laptophome.bank_eldam.data.model.donationrequests.DonationsRequests;
import com.example.laptophome.bank_eldam.data.model.editprofile.EditProfile;
import com.example.laptophome.bank_eldam.data.model.governorates.Governorates;
import com.example.laptophome.bank_eldam.data.model.notificationcount.NotificationCount;
import com.example.laptophome.bank_eldam.data.model.notificationlist.NotificationList;
import com.example.laptophome.bank_eldam.data.model.notificationsettings.NotificationSetting;
import com.example.laptophome.bank_eldam.data.model.postdetails.PostDetails;
import com.example.laptophome.bank_eldam.data.model.posts.Posts;
import com.example.laptophome.bank_eldam.data.model.register.Register;
import com.example.laptophome.bank_eldam.data.model.registernotification.RegisterNotification;
import com.example.laptophome.bank_eldam.data.model.retrivepassword.Retrivepassword;
import com.example.laptophome.bank_eldam.data.model.settings.Settings;
import com.example.laptophome.bank_eldam.data.model.togglefavourite.ToggleFavourite;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    //////////////////////////////////General/////////////////////////////////////
    @GET("governorates")
    Call<Governorates> getGovernorates();

    @GET("cities")
    Call<Cities> getCities(@Query("governorate_id") int governorate_id);

    @GET("blood-types")
    Call<BloodtypeList> getbloodtypes();

    @GET("settings")
    Call<Settings> getSettings(@Query("api_token") String api_token);

    @POST("contact")
    @FormUrlEncoded
    Call<Contacts> addContact(@Field("api_token") String api_token
            , @Field("title") String title
            , @Field("message") String message);


    /////////////////////////////////////////////////////////////////////////////////


    //////////////////////////////////Users Services////////////////////////////////
    @POST("register")
    @FormUrlEncoded
    Call<Register> Register(@Field("name") String name
            , @Field("email") String email
            , @Field("birth_date") String birth_date
            , @Field("city_id") int city_id
            , @Field("phone") String phone
            , @Field("donation_last_date") String donation_last_date
            , @Field("password") String password
            , @Field("password_confirmation") String password_confirmation
            , @Field("blood_type_id") int blood_type_id);


    @POST("login")
    @FormUrlEncoded
    Call<Register> Login(@Field("phone") String phone
            , @Field("password") String password);

    @POST("reset-password")
    @FormUrlEncoded
    Call<Retrivepassword> resetPassword(@Field("phone") String phone);

    @POST("new-password")
    @FormUrlEncoded
    Call<Retrivepassword> newPassword(@Field("phone") String phone
            , @Field("pin_code") String pin_code
            , @Field("password") String password
            , @Field("password_confirmation") String password_confirmation);

    @POST("profile")
    @FormUrlEncoded
    Call<EditProfile> editProfile(@Field("name") String name,
                                  @Field("email") String email
            , @Field("birth_date") String birth_date
            , @Field("city_id") int city_id
            , @Field("phone") String phone
            , @Field("donation_last_date") String donation_last_date
            , @Field("password") String password
            , @Field("password_confirmation") String password_confirmation
            , @Field("blood_type_id") int blood_type_id
            ,@Field("api_token") String api_token);

    @POST("profile")
    @FormUrlEncoded
    Call<EditProfile> getmyinformation(@Field("api_token") String api_token);


///////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////Posts//////////////////////////////////////

    @GET("posts")
    Call<Posts> getallposts(@Query("api_token") String api_token
            , @Query("page") int page);

    @GET("post")
    Call<PostDetails> getpostdetails(@Query("api_token") String api_token
            , @Query("post_id") int post_id);

    @GET("my-favourites")
    Call<Posts> MyFavourites(@Query("api_token") String api_token);

    @POST("post-toggle-favourite")
    @FormUrlEncoded
    Call<ToggleFavourite> AddtoFavourite(@Field("post_id") int post_id
            , @Field("api_token") String api_token);

    @GET("posts")
    Call<Posts> postsfilter(@Query("api_token") String api_token,@Query("page") int page,@Query("keyword") String keyword);
    ////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////Donations/////////////////////////
    @GET("donation-requests")
    Call<DonationsRequests> getdonationrequests(@Query("api_token") String api_token
                                                ,@Query("page") int page);

    @GET("donation-request")
    Call<DonationDetails> getdonationrequestdetails(@Query("api_token") String api_token
            , @Query("donation_id") int donation_id);

    @POST("donation-request/create")
    @FormUrlEncoded
    Call<AddDonationRequest> AddDonationRequest(@Field("api_token") String api_token
            , @Field("patient_name") String patient_name
            , @Field("patient_age") int patient_age
            , @Field("blood_type_id") int blood_type_id
            , @Field("bags_num") int bags_num
            , @Field("hospital_name") String hospital_name
            , @Field("hospital_address") String hospital_address
            , @Field("city_id") int city_id
            , @Field("phone") String phone
            , @Field("notes") String notes
            , @Field("latitude") double latitude
            , @Field("longitude") double longitude);


    @GET("donation-requests")
    Call<DonationsRequests> donationfilters(@Query("api_token") String api_token
                                            ,@Query("blood_type") String blood_type
                                            ,@Query("city_id") int city_id
                                            ,@Query("page") int page);

    ////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////Notification/////////////////////////////////////////
    @GET("notifications-count")
    Call<NotificationCount> getNotificationNumber(@Query("api_token") String api_token);

    @POST("notifications-settings")
    @FormUrlEncoded
    Call<NotificationSetting> getNotificationSettings(@Field("api_token") String api_token);

    @POST("notifications-settings")
    @FormUrlEncoded
    Call<NotificationSetting> setNotiicationSettings(@Field("api_token") String api_token,
                                                     @Field("blood_types[]") ArrayList<String> blood_types,
                                                     @Field("governorates[]") ArrayList<String> governorates);

    @GET("notifications")
    Call<NotificationList> getNotificationList(@Query("api_token") String api_token);

    @POST("register-token")
    @FormUrlEncoded
    Call<RegisterNotification> RegisterNotification(@Field("token") String token
            , @Field("api_token") String api_token
            , @Field("platform") String platform);

    @POST("remove-token")
    @FormUrlEncoded
    Call<RegisterNotification> RemoveNotification(@Field("token") String token
            , @Field("api_token") String api_token);


}