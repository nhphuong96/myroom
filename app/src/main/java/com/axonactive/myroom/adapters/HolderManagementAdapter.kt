package com.axonactive.myroom.adapters

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatCheckBox
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SwitchCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.axonactive.myroom.R
import com.axonactive.myroom.core.Constants
import com.axonactive.myroom.db.DatabaseHelper
import com.axonactive.myroom.db.model.Holder
import com.axonactive.myroom.models.RoomHolder
import com.axonactive.myroom.utils.DateUtils
import com.axonactive.myroom.validation.Validator
import com.rengwuxian.materialedittext.MaterialEditText
import de.hdodenhof.circleimageview.CircleImageView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.holder_management_item.view.*

/**
 * Created by Phuong Nguyen on 7/3/2018.
 */
class HolderManagementAdapter(private val items : ArrayList<RoomHolder>,
                               private val context: Context) : RecyclerView.Adapter<HolderManagementAdapter.ViewHolder>() {

    private val db = DatabaseHelper(context)
    private lateinit  var profileImage : ImageView
    private lateinit var imgProfileName : String

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.holder_management_item, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, position: Int) {
        viewHolder?.tvHolderName?.text = items[position].fullName
        viewHolder?.tvHolderPhone?.text = items[position].phoneNumber
        viewHolder?.imgRoomHolder?.setImageResource(context.resources.getIdentifier(items[position].imageName,
                "drawable", context.packageName))
        if (items[position].isOwner == 1) {
            viewHolder?.isDisplayMainMark?.visibility = View.VISIBLE
        }
        else {
            viewHolder?.isDisplayMainMark?.visibility = View.GONE
        }
        viewHolder?.raView?.setOnClickListener { _ : View ->
            showPartnerSettingDialog(items[position].holderId, position)
        }

    }

    private fun showPartnerSettingDialog(holderId : Long?, position: Int) {
        val builder : AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        var view : View = LayoutInflater.from(context).inflate(R.layout.dialog_header_with_switch, null)
        var title : TextView = view.findViewById(R.id.textView5)
        title.text = "View partner"
        builder.setCustomTitle(view)
        builder.setView(R.layout.partners_setting_dialog)
        builder.setNegativeButton(context.resources.getString(R.string.cancel), DialogInterface.OnClickListener { dialogInterface, _ ->
            dialogInterface.dismiss()
        })
        builder.setPositiveButton(context.resources.getString(R.string.save), null)

        val dialog : Dialog = builder.create()
        dialog.setOnShowListener { _ ->
            var etPartnerName : MaterialEditText = dialog.findViewById(R.id.partner_name_id)
            var etPartnerPhone : MaterialEditText = dialog.findViewById(R.id.partner_phone_id)
            var etPartnerIdCard : MaterialEditText= dialog.findViewById(R.id.partner_card_id)
            var etPartnerBirthday : MaterialEditText = dialog.findViewById(R.id.partner_birthday_id)
            var isOwner : AppCompatCheckBox = dialog.findViewById(R.id.isOwner)
            isOwner.visibility = View.VISIBLE
            profileImage = dialog.findViewById(R.id.partner_profile_image)
            etPartnerName.isEnabled = false
            etPartnerName.isHelperTextAlwaysShown = false

            etPartnerPhone.isEnabled = false
            etPartnerPhone.isHelperTextAlwaysShown = false

            etPartnerIdCard.isEnabled = false
            etPartnerIdCard.isHelperTextAlwaysShown = false

            etPartnerBirthday.isEnabled = false
            etPartnerBirthday.isHelperTextAlwaysShown = false

            profileImage.isEnabled = false
            isOwner.isEnabled = false

            var editable : SwitchCompat = dialog.findViewById(R.id.editable)
            editable.visibility = View.VISIBLE
            editable.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    etPartnerName.isEnabled = true
                    etPartnerPhone.isEnabled = true
                    etPartnerIdCard.isEnabled = true
                    etPartnerBirthday.isEnabled = true
                    profileImage.isEnabled = true
                    isOwner.isEnabled = true
                }
                else {
                    etPartnerName.isEnabled = false
                    etPartnerPhone.isEnabled = false
                    etPartnerIdCard.isEnabled = false
                    etPartnerBirthday.isEnabled = false
                    profileImage.isEnabled = false
                    isOwner.isEnabled = false
                }
            }

            val holder = db.getHolder(holderId!!)
            if (holder != null) {
                etPartnerName.setText(holder.fullName)
                etPartnerPhone.setText(holder.phoneNumber)
                etPartnerIdCard.setText(holder.idCard)
                etPartnerBirthday.setText(DateUtils.toSimpleDateString(holder.birthDate))
                if (holder.isOwner == 1) {
                    isOwner.isChecked = true
                }
                profileImage.setImageResource(context.resources.getIdentifier(holder.profileImage,
                        "drawable", context.packageName))
                imgProfileName = holder.profileImage!!
            }

            val clickListener : View.OnClickListener = View.OnClickListener { view ->
                if (view == profileImage) {
                    val imm : InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    showSelectProfileImageDialog()
                }
            }
            profileImage.setOnClickListener(clickListener)

            val button : Button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            button.text = context.resources.getString(R.string.save)
            button.setOnClickListener { _ : View? ->
                if (!Validator.isEmpty(etPartnerName,context) && !Validator.isEmpty(etPartnerPhone, context)) {
                    var birthdate : String? = null
                    if (etPartnerBirthday.text.isNotBlank()) {
                        if (Validator.validateBirthday(etPartnerBirthday, context, Constants.DATE_REGEX, context.resources.getString(R.string.invalid_birthday))) {
                            birthdate = etPartnerBirthday.text.toString()
                        }
                    }
                    if (etPartnerBirthday.error == null) {
                        var holder : Holder = db.getHolder(holderId)
                        if (holder != null) {
                            holder.fullName = etPartnerName.text.toString()
                            holder.phoneNumber = etPartnerPhone.text.toString()
                            holder.profileImage = imgProfileName
                            holder.birthDate = DateUtils.toSimpleDate(birthdate)
                            holder.idCard = etPartnerIdCard.text.toString()
                            if (isOwner.isChecked) {
                                holder.isOwner = 1
                            }
                            else {
                                holder.isOwner = 0
                            }
                            if (db.updateHolder(holder) == 1) {
                                items[position].fullName = holder.fullName
                                items[position].phoneNumber = holder.phoneNumber
                                items[position].birthday = holder.birthDate
                                items[position].imageName = holder.profileImage
                                items[position].idCard = holder.idCard
                                items[position].isOwner = holder.isOwner

                                notifyDataSetChanged()
                                Toasty.success(context, "Updated successfully!", Toast.LENGTH_SHORT, true).show()
                                dialog.dismiss()
                            }
                        }
                    }
                }
            }
        }
        dialog.show()
    }

    private fun initializeProfileList() : java.util.ArrayList<Int> {
        val result : java.util.ArrayList<Int> = java.util.ArrayList<Int>()
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

    private fun showSelectProfileImageDialog() {

        val gridView = GridView(context)
        val mList : java.util.ArrayList<Int> = initializeProfileList()

        gridView.adapter = (ImageAdapter(context, mList))
        gridView.numColumns = 5

        val builder : AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setView(gridView)
        var view : View = LayoutInflater.from(context).inflate(R.layout.dialog_header_with_switch, null)
        var title : TextView = view.findViewById(R.id.textView5)
        var switch : SwitchCompat = view.findViewById(R.id.editable)
        switch.visibility = View.GONE
        title.text = "Select profile image"
        builder.setCustomTitle(view)
        builder.setNeutralButton("Cancel", DialogInterface.OnClickListener() {dialog, _ ->
            dialog.cancel()
        })
        val dialog : Dialog = builder.create()
        dialog.show()

        val clickListener : AdapterView.OnItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            profileImage.setImageResource(mList[i])
            imgProfileName = context.resources.getResourceEntryName(mList[i])
            dialog.dismiss()
        }
        gridView.onItemClickListener = clickListener
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var tvHolderName: TextView
        var tvHolderPhone: TextView
        var imgRoomHolder: CircleImageView
        var isDisplayMainMark : ImageView
        var raView : View

        constructor(view: View) : super(view) {
            tvHolderPhone = view.holder_management_phone!!
            imgRoomHolder = view.holder_management_profile_image!!
            tvHolderName = view.holder_management_name!!
            isDisplayMainMark = view.is_main
            raView = view
        }
    }
}