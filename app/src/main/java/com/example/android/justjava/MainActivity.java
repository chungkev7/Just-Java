package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {
        quantity += 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {
        if (quantity != 0) {
            quantity -= 1;
        } else {
            Toast.makeText(this, "You cannot order a negative amount of coffee", Toast.LENGTH_SHORT).show();
        }

        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        EditText name = (EditText) findViewById(R.id.name_field);
        String userName = name.getText().toString();

        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(userName, price, hasWhippedCream, hasChocolate);

        displayMessage(priceMessage);

        // This intent object sends the order to an email app
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for " + name);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order
     *
     * @param hasWhippedCream is whether or not the user wants whipped cream as a topping
     * @param hasChocolate is whether or not the user wants chocolate as a topping
     * @return total price
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate){
        int basePrice = 5;

        if(hasWhippedCream) {
            basePrice += 1;
        }

        if (hasChocolate) {
            basePrice += 2;
        }

        return quantity * basePrice;
    }

    /**
     * Create a summary of the order
     *
     * @param userName is the name the user entered
     * @param hasWhippedCream is whether or not the user wants whipped cream topping
     * @param hasChocolate is whether or not the user wants chocolate topping
     * @param price of the order
     * @return text summary
     */
    private String createOrderSummary(String userName, int price, boolean hasWhippedCream, boolean hasChocolate) {
        String priceMessage = "Name: " + userName;
        priceMessage += "\n\nWhipped cream added? " + hasWhippedCream;
        priceMessage += "\n\nChocolate added? " + hasChocolate;
        priceMessage += "\n\nQuantity: " + quantity;
        priceMessage += "\n\nTotal: $" + price;
        priceMessage += "\n\nThank you!";

        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }

    /**
     * This method displays the given text on the screen
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }
}
