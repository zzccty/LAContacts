package fun.hara.lacontacts;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class ContactEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_edit);
        Intent intent = getIntent();
        String id=intent.getStringExtra("id");
        if(id!=null && !"".equals(id)){
            // 编辑时
            TextView editContactTitle = (TextView) findViewById(R.id.editContactTitle);
            editContactTitle.setText("编辑联系人");
            String name = intent.getStringExtra("name");
            String phone = intent.getStringExtra("phone");
            TextView nameTV = (TextView) findViewById(R.id.editContactName);
            TextView phoneTV = (TextView) findViewById(R.id.editContactPhone);
            nameTV.setText(name);
            phoneTV.setText(phone);
        }
    }

    public void back(View view){
        ContactEditActivity.this.finish();
    }
}
