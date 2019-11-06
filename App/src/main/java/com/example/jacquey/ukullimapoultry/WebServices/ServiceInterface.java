package com.example.jacquey.ukullimapoultry.WebServices;

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

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceInterface {

    // method,, return type ,, secondary url

    @Multipart
    @POST("UkulimaPoultry/new_user_registration.php")
    Call<NewUserRegistration> NewUserRegistrationCall(
            @Part("fname") RequestBody fname,
            @Part("lname") RequestBody lname,
            @Part("fullname") RequestBody fullname,
            @Part("email") RequestBody email,
            @Part("phone") RequestBody phone,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password
    );

    ///  user signin request
    @Multipart
    @POST("UkulimaPoultry/user_signin.php")
    Call<UserSignInRes> UserSigninCall(
            @Part("phone") RequestBody phone,
            @Part("password") RequestBody password
    );

    ///  user staffrequest
    @Multipart
    @POST("UkulimaPoultry/staff_signin.php")
    Call<StaffSignInRes> StaffSigninCall(
            @Part("phone") RequestBody phone,
            @Part("password") RequestBody password
    );

   ///  user forgot password request
    @Multipart
    @POST("UkulimaPoultry/user_forgot_password.php")
    Call<ForgotPasswordRes> UserForgotPassword(

            @Part("phone") RequestBody phone,
            @Part("password") RequestBody password
    );

    ///  create new password request
    @Multipart
    @POST("UkulimaPoultry/new_password.php")
    Call<NewPasswordRes> UserNewPassword(
            @Part("userid") RequestBody userid,
            @Part("password") RequestBody password
    );

    // get new products
    @Multipart
    @POST("UkulimaPoultry/getnewproduct.php")
    Call<NewProductRes> getNewPorduct(
            @Part("securecode") RequestBody securecode
    );

    // get chicken products
    @Multipart
    @POST("UkulimaPoultry/chickenproducts.php")
    Call<ChickenProductRes> getChickenPorduct(
            @Part("securecode") RequestBody securecode
    );
    // get duck products
    @Multipart
    @POST("UkulimaPoultry/duckproducts.php")
    Call<DuckProductRes> getDuckPorduct(
            @Part("securecode") RequestBody securecode
    );
    // get turkey products
    @Multipart
    @POST("UkulimaPoultry/turkeyproducts.php")
    Call<TurkeyProductRes> getTurkeyPorduct(
            @Part("securecode") RequestBody securecode
    );

    // get best selling products
    @Multipart
    @POST("UkulimaPoultry/getbestsellingprod.php")
    Call<NewProductRes> getBestSelling(
            @Part("securecode") RequestBody securecode
    );

    // get search products
    @Multipart
    @POST("UkulimaPoultry/searchAPI.php")
    Call<SearchProductRes> getSearchProduct(
            @Part("search") RequestBody search
    );

    // get approval
    @Multipart
    @POST("UkulimaPoultry/checkapproval.php")
    Call<approval> approvalcall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id

    );

    // get  product details
    @Multipart
    @POST("UkulimaPoultry/productscategory.php")
    Call<categoryRes> getcategory(

            @Part("prod_id") RequestBody prod_id,
            @Part("securecode") RequestBody securecode
    );

    // get product details
    @Multipart
    @POST("UkulimaPoultry/getproductdetails.php")
    Call<ProductDetail_Res> getProductDetials(
            @Part("securecode") RequestBody securecode,
            @Part("prod_id") RequestBody prod_id
    );
    // add to cart
    @Multipart
    @POST("UkulimaPoultry/add_prod_into_cart.php")
    Call<AddtoCart> addtocartcall(
            @Part("securecode") RequestBody securecode,
            @Part("prod_id") RequestBody prod_id,
            @Part("user_id") RequestBody user_id

    );
    // get user cart
    @Multipart
    @POST("UkulimaPoultry/getusercartdetails.php")
    Call<getCartDetails> getusercartcall(
            @Part("securecode") RequestBody securecode,
            @Part("qoute_id") RequestBody qoute_id,
            @Part("user_id") RequestBody user_id
    );
    // delete cart item
    @Multipart
    @POST("UkulimaPoultry/deletecartitem.php")
    Call<AddtoCart> deleteCartProd(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("prod_id") RequestBody prod_id
    );

    // edit cart qty
    @Multipart
    @POST("UkulimaPoultry/editcartitem.php")
    Call<EditCartItem> editCartQty(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("prod_id") RequestBody prod_id,
            @Part("prod_qty") RequestBody prod_qty
    );


 // get order summery
 @Multipart
 @POST("UkulimaPoultry/getordersummary.php")
 Call<OrderSummary> getOrderSummarycall(
         @Part("securecode") RequestBody securecode,
         @Part("user_id") RequestBody user_id,
         @Part("qoute_id") RequestBody qoute_id
 );

 // add new address
    @Multipart
    @POST("UkulimaPoultry/add_address.php")
    Call<AddNewAddress> addnewAddresscall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("fullname") RequestBody fullname,
            @Part("phone") RequestBody phone,
            @Part("address1") RequestBody address1,
            @Part("address2") RequestBody address2,
            @Part("city") RequestBody city,
            @Part("state") RequestBody state,
            @Part("pincode") RequestBody pincode



    );
    // get user address
    @Multipart
    @POST("UkulimaPoultry/getUserAddress.php")
    Call<GetAddress> getUserAddress(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id
    );

    // place order api
    @Multipart
    @POST("UkulimaPoultry/placeorderapi.php")
    Call<PlaceOrder> PlaceOrderCall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("address_id") RequestBody address_id,
            @Part("total_price") RequestBody total_price,
            @Part("qoute_id") RequestBody qoute_id,
            @Part("deliverymode") RequestBody deliverymode
    );



    // get order history
    @Multipart
    @POST("UkulimaPoultry/getorderhistory.php")
    Call<OrderHistoryAPI> getorderHistorycall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id
    );

    // get feedback history
    @Multipart
    @POST("UkulimaPoultry/getallfeedback.php")
    Call<feedhistoryAPI> getfeedhistorycall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id
    );

    // get order prodct details history
    @Multipart
    @POST("UkulimaPoultry/getorderhistoryproddetails.php")
    Call<GetOrderProductDetails> getorderProductdetailscall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("order_id") RequestBody order_id
    );

    // make payment
    @Multipart
    @POST("UkulimaPoultry/makepayment.php")
    Call<payAPI> makepaymentcall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id,
            @Part("order_id") RequestBody order_id,
            @Part("total_price") RequestBody total_price,
             @Part("payment_amount") RequestBody payment_amount

    );
    // CLEAR BALANCE
    @Multipart
    @POST("UkulimaPoultry/clearbalance.php")
    Call<clearbalanceAPI> clearbalancecall(
            @Part("securecode") RequestBody securecode,
            @Part("order_id") RequestBody order_id,
            @Part("total_price") RequestBody total_price,
            @Part("code") RequestBody code,
            @Part("mode") RequestBody mode,
            @Part("amount") RequestBody amount

    );

    // RECEIVE
    @Multipart
    @POST("UkulimaPoultry/receive.php")
    Call<receiveAPI> receivecall(
            @Part("securecode") RequestBody securecode,
            @Part("order_id") RequestBody order_id,
            @Part("status") RequestBody status

    );
    // code
    @Multipart
    @POST("UkulimaPoultry/code.php")
    Call<codeAPI> codecall(
            @Part("securecode") RequestBody securecode,
            @Part("order_id") RequestBody order_id,
            @Part("code") RequestBody code

    );
    // feedbackAPI
    @Multipart
    @POST("UkulimaPoultry/getfeedback.php")
    Call<feedbackAPI> feedbackcall(
            @Part("securecode") RequestBody securecode,
            @Part("feed_title") RequestBody feed_title,
            @Part("feed_comment") RequestBody feed_comment,
            @Part("user_id") RequestBody user_id

    );

    // get delivery summery
    @Multipart
    @POST("UkulimaPoultry/getdelivery.php")
    Call<DeliveryHistoryAPI> deliveryhistorycall(
            @Part("securecode") RequestBody securecode,
            @Part("user_id") RequestBody user_id

    );

    // get banner image
    @Multipart
    @POST("UkulimaPoultry/getbanner.php")
    Call<GetbannerModel> getbannerimagecall(
            @Part("securecode") RequestBody securecode
    );

}




