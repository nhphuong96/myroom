package com.axonactive.myroom.activities

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
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
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.axonactive.myroom.R
import com.axonactive.myroom.adapters.HolderRegistryAdapter
import com.axonactive.myroom.adapters.ImageAdapter
import com.axonactive.myroom.core.Constants
import com.axonactive.myroom.models.Room
import com.axonactive.myroom.models.RoomHolder
import com.axonactive.myroom.validation.Validator
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_sign_up.*

/**
 * Created by Phuong Nguyen on 5/13/2018.
 */

class SignUpActivity : AppCompatActivity() {

    private var partners : ArrayList<RoomHolder> = ArrayList()
    private var imgProfileName : String = "ic_placeholder"

    private lateinit var etCustomerName : MaterialEditText
    private lateinit var etCustomerPhone : MaterialEditText
    private lateinit var etRoomName : MaterialEditText
    private lateinit  var profileImage : ImageView
    private lateinit var etCustomerBirthday : MaterialEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)
        initializeActionBar()
        initializeViewComponent()
        validation()
        addPartners()
        initializePartnerList()
        profileSelection()
        birthdaySetting()

    }

    private fun initializeViewComponent() {
        etCustomerName = findViewById<MaterialEditText>(R.id.id_customer_name)
        etCustomerPhone = findViewById<MaterialEditText>(R.id.id_customer_phone)
        etRoomName = findViewById<MaterialEditText>(R.id.id_room_name)
        profileImage = findViewById(R.id.profile_image)

    }

    private fun profileSelection() {
        val imgProfile : ImageView = findViewById(R.id.profile_image)
        val clickListener : View.OnClickListener = View.OnClickListener { view ->
            if (view == imgProfile) {
                val imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
                showSelectProfileImageDialog()
            }
        }
        imgProfile.setOnClickListener(clickListener)
    }

    private fun initializePartnerList() {
        rv_holder_partner_list.layoutManager = LinearLayoutManager(this)
        rv_holder_partner_list.adapter = HolderRegistryAdapter(partners, this)
        rv_holder_partner_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun addPartners() {
        partners.add(RoomHolder("Nguyen Ho Phuong", "0902444505", "boy"))
        partners.add(RoomHolder("Vu Thi Bich Tien", "01222696928", "girl2"))
        partners.add(RoomHolder("Pham Thi Kim Phuong", "01248004441", "girl4"))
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
        }
        R.id.doneRegistry -> {
            val room : Room = collectInformation()
            Toast.makeText(this, room.roomName +
                    " - " + room.holders[0].fullName +
                    " - " + room.holders[0].phoneNumber +
                    " - " + room.holders[0].imageName, Toast.LENGTH_LONG).show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun collectInformation() : Room {
        val holders : ArrayList<RoomHolder> = ArrayList()
        holders.add(RoomHolder(etCustomerName.text.toString(), etCustomerPhone.text.toString(), imgProfileName))
        return Room(etRoomName.text.toString(), holders)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_on_registry_action_bar, menu)
        return true
    }

    private fun validation() {
        Validator.validateEmpty(etCustomerName, this)
        Validator.validateRegex(etCustomerPhone, this, "\\d+", "Invalid phone number!")
        Validator.validateEmpty(etRoomName, this)
    }

    private fun showSelectProfileImageDialog() {

        val gridView : GridView = GridView(this)
        val mList : ArrayList<Int> = initializeProfileList()

        gridView.adapter = (ImageAdapter(this, mList))
        gridView.numColumns = 5

        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(gridView)
        builder.setTitle("Select profile image")
        builder.setNeutralButton("Cancel", DialogInterface.OnClickListener() {dialog, _ ->
            dialog.cancel()
        })
        val dialog : Dialog = builder.create()
        dialog.show()

        val clickListener : AdapterView.OnItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            profileImage.setImageResource(mList[i])
            imgProfileName = resources.getResourceEntryName(mList[i])
            dialog.dismiss()
        }
        gridView.onItemClickListener = clickListener


    }

    private fun initializeProfileList() : ArrayList<Int> {
        val result : ArrayList<Int> = ArrayList<Int>()
        result.add(R.drawable.boy)
        result.add(R.drawable.boy1)
        result.add(R.drawable.boy2)
        result.add(R.drawable.boy3)
        result.add(R.drawable.boy4)
        result.add(R.drawable.girl)
        result.add(R.drawable.girl1)
        result.add(R.drawable.girl2)
        result.add(R.drawable.girl3)
        result.add(R.drawable.girl4)
        return result
    }

    private fun birthdaySetting() {
        val button : Button = findViewById<Button>(R.id.btnBirthdate)
        button.setOnClickListener { _: View? ->
            showBirthdayModifierDialog()
        }
    }

    private fun showBirthdayModifierDialog() {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)

        builder.setView(R.layout.birthday_setting)
        builder.setCancelable(false)
        builder.setTitle("Update birthday")
        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.dismiss()
        })
        builder.setPositiveButton("Update", null)

        val dialog : Dialog = builder.create()
        dialog.setOnShowListener { dialogInterface ->

            etCustomerBirthday = dialog.findViewById(R.id.id_customer_birthday)

            val button : Button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            button.text = resources.getString(R.string.accept)
            button.setOnClickListener { view: View? ->
                if (Validator.validateBirthday(etCustomerBirthday, Constants.DATE_REGEX, "Invalid birthday format!")) {
                    Toast.makeText(this, etCustomerBirthday.text.toString(), Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(this, "Cai shit!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        dialog.show()



    }

}