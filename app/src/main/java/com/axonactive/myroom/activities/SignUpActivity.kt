package com.axonactive.myroom.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.axonactive.myroom.R
import com.axonactive.myroom.adapters.HolderRegistryAdapter
import com.axonactive.myroom.models.RoomHolder
import com.axonactive.myroom.validation.Validator
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_sign_up.*

/**
 * Created by Phuong Nguyen on 5/13/2018.
 */

class SignUpActivity : AppCompatActivity() {

    var partners : ArrayList<RoomHolder> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)
        initializeActionBar()
        validation()
        addPartners()
        initializePartnerList()
        profileSelection()

    }

    private fun profileSelection() {
        val imgProfile : ImageView = findViewById(R.id.profile_image);
        val clickListener : View.OnClickListener = View.OnClickListener { view ->
            if (view.equals(imgProfile)) {
                showSelectProfileImageDialog()
            }
        };
        imgProfile.setOnClickListener(clickListener);
    }

    private fun initializePartnerList() {
        rv_holder_partner_list.layoutManager = LinearLayoutManager(this);
        rv_holder_partner_list.adapter = HolderRegistryAdapter(partners, this);
        rv_holder_partner_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun addPartners() {
        partners.add(RoomHolder("Nguyen Ho Phuong", "0902444505", R.drawable.boy))
        partners.add(RoomHolder("Vu Thi Bich Tien", "01222696928", R.drawable.girl2))
        partners.add(RoomHolder("Pham Thi Kim Phuong", "01248004441", R.drawable.girl4))
    }

    private fun initializeActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "Register"
        supportActionBar?.subtitle = "Customer Information"
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#ff9933")))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        };
        R.id.doneRegistry -> {
            Toast.makeText(this, "Done!", Toast.LENGTH_LONG).show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_on_registry_action_bar, menu);
        return true;
    }

    private fun validation() {
        var etCustomerName = findViewById<MaterialEditText>(R.id.id_customer_name)
        var etCustomerPhone = findViewById<MaterialEditText>(R.id.id_customer_phone)
        var etRoomName = findViewById<MaterialEditText>(R.id.id_room_name)

        Validator.validateEmpty(etCustomerName, this)
        Validator.validateRegex(etCustomerPhone, this, "\\d+", "Invalid phone number!")
        Validator.validateEmpty(etRoomName, this)

    }

    private fun showSelectProfileImageDialog() {
        var gridView : GridView = GridView(this);
        var mList : ArrayList<Int> = ArrayList<Int>();
        for (i in 1..10) {
            mList.add(i);
        }

        gridView.adapter = (ArrayAdapter(this, android.R.layout.simple_list_item_1, mList));
        gridView.numColumns = 5;
        gridView.onItemClickListener = (AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(this, position, Toast.LENGTH_LONG).show()
        });

        val builder : AlertDialog.Builder = AlertDialog.Builder(this);
        builder.setView(gridView);
        builder.setTitle("Goto");
        builder.show();
    }
}