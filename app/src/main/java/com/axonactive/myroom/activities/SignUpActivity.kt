package com.axonactive.myroom.activities

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.axonactive.myroom.R
import com.axonactive.myroom.adapters.HolderRegistryAdapter
import com.axonactive.myroom.adapters.ImageAdapter
import com.axonactive.myroom.core.Constants
import com.axonactive.myroom.models.RoomHolder
import com.axonactive.myroom.utils.DateUtils
import com.axonactive.myroom.validation.Validator
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.*
import com.axonactive.myroom.db.DatabaseHelper
import com.axonactive.myroom.utils.RoomStatus


/**
 * Created by Phuong Nguyen on 5/13/2018.
 */
class SignUpActivity : AppCompatActivity() {

    private val db = DatabaseHelper(this)

    private var partners : ArrayList<RoomHolder> = ArrayList()

    private lateinit var etCustomerName : MaterialEditText
    private lateinit var etCustomerPhone : MaterialEditText
    private lateinit var etRoomName : MaterialEditText
    private lateinit  var profileImage : ImageView
    private var imgProfileName : String = "ic_placeholder"

    private lateinit var etPartnerName : MaterialEditText
    private lateinit var etPartnerPhone : MaterialEditText
    private lateinit var etPartnerIdCard : MaterialEditText
    private lateinit var etPartnerBirthday : MaterialEditText
    private var partnerProfileImageName : String = "ic_placeholder"
    private lateinit var partnerProfileImg : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)
        initializeViewComponent()
        initializeActionBar()
        validation()
        addPartners()
        initializePartnerList()
        profileSelection()
        addMoreSelection()

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
                showSelectProfileImageDialog(true)
            }
        }
        imgProfile.setOnClickListener(clickListener)
    }

    private fun initializePartnerList() {
        rv_holder_partner_list.setEmptyView(findViewById(R.id.empty_list_placeholder_id))
        rv_holder_partner_list.layoutManager = LinearLayoutManager(this)
        rv_holder_partner_list.adapter = HolderRegistryAdapter(partners, this)
        rv_holder_partner_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

    }

    private fun addPartners() {
    }

    private fun initializeActionBar() {
        var toolbar : Toolbar = findViewById<Toolbar>(R.id.toolbar)
        var title : TextView = toolbar.findViewById<TextView>(R.id.toolbar_title)
        title.text = "Register"
        title.textSize = 14f
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_backspace))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.blue)))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        android.R.id.home -> {
            finish()
            true
        }
        R.id.doneRegistry -> {
            if (!Validator.isEmpty(etRoomName, this)
                && !Validator.isEmpty(etCustomerName, this)
                && !Validator.isEmpty(etCustomerPhone, this)) {
                val room = com.axonactive.myroom.db.model.Room(null, etRoomName.text.toString(), RoomStatus.INIT)
                val roomId : Long = db.createRoom(room)
                if (roomId != null) {
                    val holder = com.axonactive.myroom.db.model.Holder(null, etCustomerName.text.toString(),
                            etCustomerPhone.text.toString(),
                            null, null, null, imgProfileName, null, roomId)

                    if (db.createHolder(holder) != null) {
                        for (partner in partners) {
                            val partner = com.axonactive.myroom.db.model.Holder(null, partner.fullName,
                                    partner.phoneNumber, partner.idCard, partner.birthday, null, null, null, roomId)
                            if (db.createHolder(partner) == null) {
                                Toast.makeText(this, String.format(resources.getString(R.string.created_partner_unsuccessfully), partner.fullName), Toast.LENGTH_LONG).show()
                                break
                            }
                        }
                        Toast.makeText(this, String.format(resources.getString(R.string.created_room_successful), etRoomName.text.toString()), Toast.LENGTH_LONG).show()
                        finish()
                    }
                }



            }
            true
        }
        else -> super.onOptionsItemSelected(item)
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

    private fun showSelectProfileImageDialog(isMainHolder : Boolean) {

        val gridView = GridView(this)
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
            if (isMainHolder) {
                profileImage.setImageResource(mList[i])
                imgProfileName = resources.getResourceEntryName(mList[i])
            }
            else {
                partnerProfileImg.setImageResource(mList[i])
                partnerProfileImageName = resources.getResourceEntryName(mList[i])
            }

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

//    private fun showBirthdayModifierDialog() {
//        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
//
//        builder.setView(R.layout.birthday_setting_dialog)
//        builder.setCancelable(false)
//        builder.setTitle("Update birthday")
//        builder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, _ ->
//            dialogInterface.dismiss()
//        })
//        builder.setPositiveButton("Update", null)
//
//        val dialog : Dialog = builder.create()
//        dialog.setOnShowListener { _ ->
//            etCustomerBirthday = dialog.findViewById(R.id.id_customer_birthday)
//            val button : Button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
//            button.text = resources.getString(R.string.accept)
//            button.setOnClickListener { _: View? ->
//                if (Validator.validateBirthday(etCustomerBirthday, this ,Constants.DATE_REGEX, "Invalid birthday format!")) {
//                    birthday = etCustomerBirthday.text.toString()
//                    dialog.dismiss()
//                }
//            }
//        }
//        dialog.show()
//    }

    private fun addMoreSelection() {
        val btnAddMore : Button = findViewById(R.id.btn_add_partner_id)
        btnAddMore.setOnClickListener { _ ->
            showPartnerSettingDialog()
        }
    }

    private fun showPartnerSettingDialog() {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Add partner")
        builder.setCancelable(false)
        builder.setView(R.layout.partners_setting_dialog)
        builder.setNegativeButton(resources.getString(R.string.cancel), DialogInterface.OnClickListener { dialogInterface, _ ->
            dialogInterface.dismiss()
        })
        builder.setPositiveButton(resources.getString(R.string.add), null)

        val dialog : Dialog = builder.create()
        dialog.setOnShowListener { _ ->
            etPartnerName = dialog.findViewById(R.id.partner_name_id)
            etPartnerPhone = dialog.findViewById(R.id.partner_phone_id)
            etPartnerIdCard = dialog.findViewById(R.id.partner_card_id)
            etPartnerBirthday = dialog.findViewById(R.id.partner_birthday_id)
            partnerProfileImg = dialog.findViewById(R.id.partner_profile_image)
            val clickListener : View.OnClickListener = View.OnClickListener { view ->
                if (view == partnerProfileImg) {
                    val imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    showSelectProfileImageDialog(false)
                }
            }
            partnerProfileImg.setOnClickListener(clickListener)

            val button : Button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            button.text = resources.getString(R.string.add)
            button.setOnClickListener { _ : View? ->
                if (!Validator.isEmpty(etPartnerName,this) && !Validator.isEmpty(etPartnerPhone, this)) {
                    var birthdate : String? = null
                    if (etPartnerBirthday.text.isNotBlank()) {
                        if (!Validator.validateBirthday(etPartnerBirthday, this, Constants.DATE_REGEX, resources.getString(R.string.invalid_birthday))) {
                            birthdate = etPartnerBirthday.text.toString()
                        }
                    }
                    val partner = RoomHolder(null,
                            etPartnerName.text.toString(),
                            etPartnerPhone.text.toString(),
                            partnerProfileImageName,
                            DateUtils.toSimpleDate(birthdate),
                            etPartnerIdCard.text.toString())
                    partners.add(partner)
                    Toast.makeText(this, "Added partner successfully!", Toast.LENGTH_SHORT).show()
                    rv_holder_partner_list.adapter.notifyDataSetChanged()
                    dialog.dismiss()
                }
            }
        }
        dialog.show()
    }

}