/**
 * Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p/>
 * package com.example.android.justjava;
 */
package com.example.user.coffee;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import java.lang.String;

import java.text.NumberFormat;



/**
 * This app displays an order form to order coffee.
 *
 */
 interface AsyncResponse {
   public void processFinish(String output);
}

public class MainActivity extends Activity implements AsyncResponse {
    int quantity = 2;
    static String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);


        // gp.delegate=this;

    }

    // GpsLocation gp=new GpsLocation(this);

    public void processFinish(String output) {
        //Here you will receive the result fired from async class
        //of onPostExecute(result) method.
        address = output;
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void increment(View view) {
        if (quantity == 100) {
            Toast.makeText(this, "you cannot order more than 100 coffees", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;


        display(quantity);

    }


    public void decrement(View view) {

        if (quantity == 1) {
            Toast.makeText(this, "you cannot order less than 1 coffee", Toast.LENGTH_SHORT).show();
            return;
        }

        quantity = quantity - 1;


        display(quantity);
    }

    public void submitOrder(View view) {
        String st;
        String name;


        int price;
        display(quantity);

        EditText ed = (EditText) findViewById(R.id.nametxt);
        name = ed.getText().toString();

        Boolean b = false;
        Boolean b1 = false;
        CheckBox cb = (CheckBox) findViewById(R.id.chkbox);
        CheckBox cb1 = (CheckBox) findViewById(R.id.chkbox2);
        if (cb.isChecked() == true) {
            b = true;
        }
        if (cb1.isChecked() == true) {
            b1 = true;
        }
        price = calculatePrice(b, b1);


        st = createOrderSummary(price, b, b1, name);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this

        intent.putExtra(Intent.EXTRA_SUBJECT, "order summary for" + name);


        intent.putExtra(Intent.EXTRA_TEXT, st + address);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        //displayPrice(st);
    }

    /**
     * This method displays the given quantity value on the screen.
     * ;
     */
    public String createOrderSummary(int price, Boolean b, Boolean bb, String nm) {


        String str;
        str = "Name: " + nm + "\n ADD Whipped Cream:" + b + "\n ADD Chocolate:" + bb + "\n Quantity:" + quantity + "\n Total:" + price + "$";
        return str;
    }

    public int calculatePrice(boolean b, boolean b1) {
        if (b == true && b1 == false)
            return (5 * quantity + quantity * 1);
        else if (b == true && b1 == true)
            return (5 * quantity + quantity * 1 + quantity * 2);
        else if (b == false && b1 == true)
            return (5 * quantity + quantity * 2);
        else
            return 5 * quantity;
    }

    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given price on the screen.
     */

    public void getLocation(View view) {

        GpsLocation gp = new GpsLocation(getApplicationContext());
        //PlaceholderFragment pf = new PlaceholderFragment();
        gp.delegate = this;

        gp.execute();


    }


    public class GpsLocation extends AsyncTask<Void, Void, String> {
        private static final String TAG = "MyActivity";


        String add;
        Context cnt;

        GpsLocation(Context ct) {
            cnt = ct;
        }


        AsyncResponse delegate = null;



    @Override
    protected String doInBackground(Void... voids) {
        GPSTracker mgp = new GPSTracker(cnt);


        try {


            add = printAddress(mgp);

        } catch (IOException e) {
            add = null;

        }
        return add;

    }

    public String printAddress(GPSTracker mGPS) throws IOException {

        Log.d("check_tag", "can get loc" + mGPS.canGetLocation());


        if (mGPS.canGetLocation) {
            mGPS.getLocation();
            Geocoder geocoder;
            List<Address> addresses;

            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault() );

            addresses = geocoder.getFromLocation(mGPS.getLatitude(), mGPS.getLongitude(), 1);
            Log.d("try_tag", "lat is" + mGPS.getLatitude() + mGPS.getLongitude());
            Log.d("add_tag", addresses.toString());

            // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Log.d(TAG, address);
            return address;
            //  text.setText("Lat"+mGPS.getLatitude()+"Lon"+mGPS.getLongitude());
        } else {
            return null;


        }
    }

    protected void onPostExecute(String add) {
        address = add;
        delegate.processFinish(add);
    }
}
        }





