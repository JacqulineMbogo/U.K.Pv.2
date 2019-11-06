package com.example.jacquey.ukullimapoultry.WebServices;

import com.example.jacquey.ukullimapoultry.BuildConfig;

import com.example.jacquey.ukullimapoultry.Utility.Constant;
import com.example.jacquey.ukullimapoultry.beanResponse.AddNewAddress;
import com.example.jacquey.ukullimapoultry.beanResponse.AddtoCart;
import com.example.jacquey.ukullimapoultry.beanResponse.ChickenProductRes;
import com.example.jacquey.ukullimapoultry.beanResponse.DeliveryHistoryAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.DuckProductRes;
import com.example.jacquey.ukullimapoultry.beanResponse.EditCartItem;
import com.example.jacquey.ukullimapoultry.beanResponse.ForgotPasswordRes;
import com.example.jacquey.ukullimapoultry.beanResponse.GetAddress;
import com.example.jacquey.ukullimapoultry.beanResponse.GetOrderProductDetails;
import com.example.jacquey.ukullimapoultry.beanResponse.GetbannerModel;
import com.example.jacquey.ukullimapoultry.beanResponse.NewPasswordRes;
import com.example.jacquey.ukullimapoultry.beanResponse.NewProductRes;
import com.example.jacquey.ukullimapoultry.beanResponse.NewUserRegistration;
import com.example.jacquey.ukullimapoultry.beanResponse.OrderHistoryAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.OrderSummary;
import com.example.jacquey.ukullimapoultry.beanResponse.PlaceOrder;
import com.example.jacquey.ukullimapoultry.beanResponse.ProductDetail_Res;
import com.example.jacquey.ukullimapoultry.beanResponse.SearchProductRes;
import com.example.jacquey.ukullimapoultry.beanResponse.StaffSignInRes;
import com.example.jacquey.ukullimapoultry.beanResponse.TurkeyProductRes;
import com.example.jacquey.ukullimapoultry.beanResponse.UserSignInRes;
import com.example.jacquey.ukullimapoultry.beanResponse.approval;
import com.example.jacquey.ukullimapoultry.beanResponse.categoryRes;
import com.example.jacquey.ukullimapoultry.beanResponse.clearbalanceAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.codeAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.feedbackAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.feedhistoryAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.getCartDetails;
import com.example.jacquey.ukullimapoultry.beanResponse.payAPI;
import com.example.jacquey.ukullimapoultry.beanResponse.receiveAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class ServiceWrapper  {

    private ServiceInterface mServiceInterface;

    public ServiceWrapper(Interceptor mInterceptorheader) {
        mServiceInterface = getRetrofit(mInterceptorheader).create(ServiceInterface.class);
    }

    public Retrofit getRetrofit(Interceptor mInterceptorheader) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient mOkHttpClient = null;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(Constant.API_CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(Constant.API_READ_TIMEOUT, TimeUnit.SECONDS);

//        if (BuildConfig.DEBUG)
//            builder.addInterceptor(loggingInterceptor);

        if (BuildConfig.DEBUG) {
//            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }


        mOkHttpClient = builder.build();
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(mOkHttpClient)
                .build();
        return retrofit;
    }

    public Call<NewUserRegistration> newUserRegistrationCall( String fname,String lname,String fullname, String email,String phone, String username, String password){
        return mServiceInterface.NewUserRegistrationCall(convertPlainString(fname),convertPlainString(lname), convertPlainString(fullname),convertPlainString(email), convertPlainString(phone), convertPlainString( username),
                convertPlainString(password));
    }
    ///  user signin
    public Call<UserSignInRes> UserSigninCall(String phone, String password){
        return mServiceInterface.UserSigninCall(convertPlainString(phone),  convertPlainString(password));
    }

    ///  user signin
    public Call<StaffSignInRes> StaffSigninCall(String phone, String password){
        return mServiceInterface.StaffSigninCall(convertPlainString(phone),  convertPlainString(password));
    }

    // get approval status
    public Call<approval> approvalcall(String securcode, String user_id){
        return mServiceInterface.approvalcall(convertPlainString(securcode), convertPlainString(user_id));
    }
   ///  forgot password
    public Call<ForgotPasswordRes> UserForgotPassword(String phone, String password){
        return mServiceInterface.UserForgotPassword(convertPlainString(phone),  convertPlainString(password));
    }
    ///  user new password
    public Call<NewPasswordRes> UserNewPassword(String userid, String password){
        return mServiceInterface.UserNewPassword(convertPlainString(userid), convertPlainString(password));

    }

    ///  new product details
    public Call<NewProductRes> getNewProductRes(String securcode){
        return mServiceInterface.getNewPorduct(convertPlainString(securcode));
    }

    ///  new search product details
    public Call<SearchProductRes> getSearchProductRes(String search){
        return mServiceInterface.getSearchProduct(convertPlainString(search));
    }
    ///  chicken product details
    public Call<ChickenProductRes> getChickenProductRes(String securcode){
        return mServiceInterface.getChickenPorduct(convertPlainString(securcode));
    }
    ///  duck product details
    public Call<DuckProductRes> getDuckProductRes(String securcode){
        return mServiceInterface.getDuckPorduct(convertPlainString(securcode));
    }
    ///  chicken product details
    public Call<TurkeyProductRes> getTurkeyProductRes(String securcode){
        return mServiceInterface.getTurkeyPorduct(convertPlainString(securcode));
    }
    ///  new product categorie
    public Call<categoryRes> getcategoryRes(String prod_id,String securcode){
        return mServiceInterface.getcategory( convertPlainString(prod_id ), convertPlainString(securcode));
    }



    ///  best selling product details
    public Call<NewProductRes> getBestselling(String securcode){
        return mServiceInterface.getBestSelling(convertPlainString(securcode));
    }
    // get product detials
       public Call<ProductDetail_Res> getProductDetails(String securcode, String prod_id){
        return mServiceInterface.getProductDetials(convertPlainString(securcode), convertPlainString(prod_id));
    }


    // add to cart
    public Call<AddtoCart> addtoCartCall(String securcode, String prod_id, String user_id){
        return mServiceInterface.addtocartcall(convertPlainString(securcode), convertPlainString(prod_id),convertPlainString(user_id) );
    }

    // get user cart
    // add to cart
    public Call<getCartDetails> getCartDetailsCall(String securcode, String qoute_id, String user_id){
        return mServiceInterface.getusercartcall(convertPlainString(securcode), convertPlainString(qoute_id),convertPlainString(user_id) );
    }

    // delete cart item
    public Call<AddtoCart> deletecartprod(String securcode, String user_id, String prod_id){
        return mServiceInterface.deleteCartProd(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(prod_id) );
    }
    // edit cart item
    public Call<EditCartItem> editcartcartprodqty(String securcode, String user_id, String prod_id, String prod_qty){
        return mServiceInterface.editCartQty(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(prod_id),  convertPlainString(prod_qty) );
    }


    // get order summery
    public Call<OrderSummary> getOrderSummarycall(String securcode, String user_id, String qoute_id){
        return mServiceInterface.getOrderSummarycall(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(qoute_id) );
    }

    // add new address
    public Call<AddNewAddress> addNewAddressCall(String securcode, String user_id, String fullname, String phone,  String address1, String adress2, String city, String state,
                                                 String pincode){
        return mServiceInterface.addnewAddresscall(convertPlainString(securcode), convertPlainString(user_id),convertPlainString(fullname),convertPlainString(phone), convertPlainString(address1)
                , convertPlainString(adress2), convertPlainString(city), convertPlainString(state), convertPlainString(pincode));
    }
    // get order summery
    public Call<GetAddress> getUserAddresscall(String securcode, String user_id){
        return mServiceInterface.getUserAddress(convertPlainString(securcode), convertPlainString(user_id) );
    }
    // get place order api
    public Call<PlaceOrder> placceOrdercall(String securcode, String user_id,   String address_id,
                                            String total_price, String qoute_id, String delivermode){
        return mServiceInterface.PlaceOrderCall(convertPlainString(securcode), convertPlainString(user_id),
                convertPlainString(address_id), convertPlainString(total_price), convertPlainString(qoute_id), convertPlainString( delivermode));
    }

    // get order history
    public Call<OrderHistoryAPI> getorderhistorycall(String securcode, String user_id){
        return mServiceInterface.getorderHistorycall(convertPlainString(securcode), convertPlainString(user_id) );
    }
    // get feedback history
    public Call<feedhistoryAPI> getfeedhistorycall(String securcode, String user_id){
        return mServiceInterface.getfeedhistorycall(convertPlainString(securcode), convertPlainString(user_id) );
    }
    // get order prodcut detais history
    public Call<GetOrderProductDetails> getorderproductcall(String securcode, String user_id, String order_id){
        return mServiceInterface.getorderProductdetailscall(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(order_id) );
    }

    // get order prodcut detais history
    public Call<payAPI> makepaymentcall(String securcode, String user_id, String order_id , String total_price, String payment_amount){
        return mServiceInterface. makepaymentcall(convertPlainString(securcode), convertPlainString(user_id), convertPlainString(order_id) , convertPlainString(total_price),  convertPlainString(payment_amount) );
    }
    public Call<clearbalanceAPI> clearbalancecall(String securcode,  String order_id , String total_price, String code,String mode,String amount){
        return mServiceInterface. clearbalancecall(convertPlainString(securcode), convertPlainString(order_id) , convertPlainString(total_price),  convertPlainString(code),  convertPlainString(mode), convertPlainString(amount) );
    }

    public Call<receiveAPI> receivecall(String securcode, String order_id , String status){
        return mServiceInterface. receivecall(convertPlainString(securcode), convertPlainString(order_id) , convertPlainString(status) );
    }
    ///receive code
    public Call<codeAPI> codecall(String securcode, String order_id , String code){
        return mServiceInterface. codecall(convertPlainString(securcode), convertPlainString(order_id) , convertPlainString(code) );
    }

    public Call<feedbackAPI> feedbackcall(String securcode, String feed_title , String feed_comment, String user_id){
        return mServiceInterface. feedbackcall(convertPlainString(securcode), convertPlainString(feed_title) , convertPlainString(feed_comment), convertPlainString(user_id) );
    }
    // get order history
    public Call<DeliveryHistoryAPI> deliveryhistorycall(String securcode, String user_id){
        return mServiceInterface.deliveryhistorycall(convertPlainString(securcode), convertPlainString(user_id) );
    }

    // get banner image
    public Call<GetbannerModel> getbannerModelCall(String securcode){
        return mServiceInterface.getbannerimagecall(convertPlainString(securcode) );
    }
    // convert aa param into plain text
    public RequestBody convertPlainString(String data){
        RequestBody plainString = RequestBody.create(MediaType.parse("text/plain"), data);
        return  plainString;
    }

}


