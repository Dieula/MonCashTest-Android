package sylvie.dieula.moncashapitest;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.digicelgroup.moncash.APIContext;
import com.digicelgroup.moncash.exception.MonCashRestException;
import com.digicelgroup.moncash.http.Constants;
import com.digicelgroup.moncash.payments.Payment;
import com.digicelgroup.moncash.payments.PaymentCapture;
import com.digicelgroup.moncash.payments.PaymentCreator;
import com.digicelgroup.moncash.payments.TransactionId;
import org.apache.http.HttpStatus;


import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    ImageView service1 ;
    ImageView service2 ;

    private APIContext apiContext;

    PaymentCreator paymentCreator;
    Payment payment;

    private static final Logger logger = Logger
            .getLogger(PaymentCreator.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        service1 = findViewById(R.id.ivService1);
        service2 = findViewById(R.id.ivService2);


        apiContext = new APIContext(getString(R.string.client_id),
                getString(R.string.client_secret), Constants.SANDBOX);

        service1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayService();
            }

        });

        service2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayService();
            }

        });
    }

    private void PayService()

    {
        paymentCreator = new PaymentCreator();
        payment = new Payment();
        payment.setOrderId(System.currentTimeMillis()+"");
        payment.setAmount(Double.parseDouble("50"));
        PaymentCreator creator = null;
        try {
            creator = paymentCreator.execute(apiContext, PaymentCreator.class, payment);
            if(creator.getStatus() !=null &&
                    creator.getStatus().compareTo(HttpStatus.SC_ACCEPTED+"")==0){
                logger.info("");
                //creator.redirectUri() method return the payment gateway url
                logger.info(creator.redirectUri());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(creator.redirectUri()));
                startActivity(browserIntent);
            }else if(creator.getStatus()==null){
                logger.warning("Error");
                logger.warning(creator.getError());
                logger.warning(creator.getError_description());
            }else{
                logger.warning("Error");
                logger.warning(creator.getStatus());
                logger.warning(creator.getError());
                logger.warning(creator.getMessage());
                logger.warning(creator.getPath());
            }
        } catch (MonCashRestException e) {
            e.printStackTrace();
        }
    }

}
