package pt.oitoo.portooculto.view.binding;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.graphics.ColorUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import pt.oitoo.portooculto.R;
import pt.oitoo.portooculto.util.FirebaseUtil;

public class BindingAdapters {

    /**
     * Receives a boolean value of the result of the field validation and the error text to be displayed.
     * Also receives a boolean value of all fields being empty, preventing all fields being activated when the form is called.
     * @param textInputLayout is the view
     * @param isValid is the boolean value of the fields validation
     * @param errorText is the error text to be displayed when the field is invalid
     * @param isFirstRun is a boolean value of all fields being empty
     */
    @BindingAdapter(value={"isValid", "errorText", "isAllEmpty"})
    public static void isValid(TextInputLayout textInputLayout, boolean isValid, String errorText, boolean isFirstRun) {
        if(!isFirstRun){
            textInputLayout.setError(isValid ? null : errorText);
        }
    }

    /**
     * Receives a boolean value and changes visibility of the view based on the boolean value
     * @param view is the view
     * @param bool is the boolean value
     */
    @BindingAdapter("setVisibility")
    public static void setVisibility(View view, boolean bool) {
        view.setVisibility(bool ? View.VISIBLE : View.GONE);
    }

    /**
     * Receives a boolean that indicates a process is being validated
     * @param view the view that is set visible when process is validating
     * @param isValidating is the boolean value of the validation
     */
    @BindingAdapter("setValidatingProgressVisibility")
    public static void setValidatingProgressVisibility(View view, boolean isValidating) {
        view.setVisibility(isValidating ? View.VISIBLE : View.INVISIBLE);
    }

    @BindingAdapter("clickable")
    public static void clickable(View view, boolean isValidating) {
        view.setClickable(!isValidating);
    }

    @BindingAdapter("firstLetter")
    public static void setFirstLetter(TextView textView, String name) {
        if(name != null){
            textView.setText(String.valueOf(name.charAt(0)).toUpperCase());
        }
    }
/*
    @BindingAdapter("setPaymentOptions")
    public static void setPaymentOptions(CollapsibleCard collapsibleCard, List<String> paymentOptions) {
        if(paymentOptions != null){
            for(String options : paymentOptions){
                collapsibleCard.setCardDescription(options + '\n');
            }
        }
    }*/

    @BindingAdapter("setGlideImage")
    public static void setGlideImage(ImageView imageView, ArrayList<String> photos){
        if(!photos.isEmpty()) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(imageView.getContext()).load(FirebaseUtil.getPhotoRef(photos.get(0))).apply(options).into(imageView);
        } else {
            imageView.setImageResource(android.R.color.transparent);
        }
    }

    @BindingAdapter("setGlideDrawable")
    public static void setGlideDrawable(ImageView imageView, Bitmap image){
        if(image != null) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(imageView.getContext()).load(image).apply(options).into(imageView);
        } else {
            imageView.setImageResource(android.R.color.transparent);
        }
    }

    @BindingAdapter("setBuildingState")
    public static void setBuildingState(ImageView imageView, String buildingState){
        if(buildingState != null) {

            int imgResource;
            switch(buildingState){
                case "DEGRADED":
                case "DEGRADADO":
                    imgResource = R.drawable.ic_degradado;
                    break;
                case "RUINS":
                case "RUÍNA":
                    imgResource = R.drawable.ic_ruina;
                    break;
                case "TERRAIN":
                case "TERRENO":
                    imgResource = R.drawable.ic_terreno;
                    break;
                case "GOOD CONDITION":
                case "BOM ESTADO":
                    imgResource = R.drawable.ic_bom_estado;
                    break;
                default:
                    imgResource = android.R.color.transparent;
                    break;
            }

            imageView.setImageResource(imgResource);
        } else {
            imageView.setImageResource(android.R.color.transparent);
        }
    }

    @BindingAdapter("setCategory")
    public static void setCategory(TextView textView, int categoryId){

        String categoryName = "";

        switch(categoryId){
            case 1:
                categoryName = "Oriental";
                break;
            case 2:
                categoryName = "Pizzeria";
                break;
            case 3:
                categoryName = "Fast Food";
                break;
            case 4:
                categoryName = "Bars and Pubs";
                break;
            case 5:
                categoryName = "Hamburgers";
                break;
            case 6:
                categoryName = "Barbecue";
                break;
            case 7:
                categoryName = "Snack bar";
                break;
            case 8:
                categoryName = "Hot Dogs";
                break;
            case 9:
                categoryName = "Buffet";
                break;
            case 10:
                categoryName = "Italian";
                break;
            case 11:
                categoryName = "Arabic";
                break;
            case 12:
                categoryName = "Mexican";
                break;
            case 13:
                categoryName = "French";
                break;
            case 14:
                categoryName = "Portuguese";
                break;
            case 15:
                categoryName = "Healthy";
                break;
            case 16:
                categoryName = "Vegan";
                break;
            case 17:
                categoryName = "Candyshop";
                break;
            case 18:
                categoryName = "Regional";
                break;
            default:
                categoryName = "Error";
                break;
        }

        textView.setText(categoryName);
        if(categoryName.equals("Error")){
            textView.setTextColor(Color.RED);
        } else {
            textView.setTextColor(Color.parseColor("#554848"));
        }
    }

    @BindingAdapter("setDiscountCategory")
    public static void setDiscountCategory(TextView textView, int categoryId){

        String categoryName = "";

        switch(categoryId){
            case 1:
                categoryName = "Rodízio";
                break;
            case 2:
                categoryName = "Desconto";
                break;
            case 3:
                categoryName = "Combo";
                break;
            case 4:
                categoryName = "Em Dobro";
                break;
            case 5:
                categoryName = "Happy Hour";
                break;
            default:
                categoryName = "Error";
                break;
        }

        textView.setText(categoryName);
        if(categoryName.equals("Error")){
            textView.setTextColor(Color.RED);
        } else {
            textView.setTextColor(Color.parseColor("#554848"));
        }
    }

    @BindingAdapter("textColor")
    public static void setTextColor(TextView textView, String colorHex){
        if(colorHex != null){
            if(ColorUtils.calculateLuminance(Color.parseColor(colorHex)) < 0.5) {
                textView.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                textView.setTextColor(Color.parseColor("#000000"));
            }
        }
    }

}
