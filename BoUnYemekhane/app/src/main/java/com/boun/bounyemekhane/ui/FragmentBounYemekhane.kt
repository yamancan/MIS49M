package com.boun.bounyemekhane.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.boun.bounyemekhane.R
import com.boun.bounyemekhane.models.Food
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class FragmentBounYemekhane : Fragment() {

    private lateinit var txtFoodDate: TextView
    private lateinit var txtNoonMenu: TextView
    private lateinit var txtEveningMenu: TextView
    private lateinit var linearChangeDay: LinearLayout
    private lateinit var btnChangeDay: Button
    private lateinit var btnGununYemegi: Button
    private lateinit var imgBtnListen: ImageView

    private lateinit var elDates: Elements
    private lateinit var elSoups: Elements
    private lateinit var elMains: Elements
    private lateinit var elVegetarians: Elements
    private lateinit var elAuxiliaries: Elements
    private lateinit var elSnacks: Elements
    private lateinit var tts: TextToSpeech

    private val SHARED_PREF_FILE = "shared_pref_file_name"

    private var dateToGettingMenu = ""

    private val foodList = ArrayList<Food>()

    val SITE_URL = "https://yemekhane.boun.edu.tr/aylik-menu/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_boun_yemekhane, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeVariables()
        onClickListeners()
    }

    override fun onPause() {
        super.onPause()

        if (tts.isSpeaking){
            tts.stop()
            imgBtnListen.setImageResource(R.drawable.volume_up)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun initializeVariables() {
        txtFoodDate = requireView().findViewById(R.id.txtFoodDate)
        txtNoonMenu = requireView().findViewById(R.id.txtNoonMenu)
        txtEveningMenu = requireView().findViewById(R.id.txtEveningMenu)
        linearChangeDay = requireView().findViewById(R.id.linearChangeDay)
        btnChangeDay = requireView().findViewById(R.id.btnChangeDay)
        btnGununYemegi = requireView().findViewById(R.id.btnGununYemegi)
        imgBtnListen = requireView().findViewById(R.id.imgBtnListen)
        tts = TextToSpeech(requireContext()) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale("tr_TR")
            }
        }

        WebScratch().execute()
    }

    private fun onClickListeners() {
        linearChangeDay.setOnClickListener {
            showDatePickerDialog()
        }

        btnChangeDay.setOnClickListener {
            showDatePickerDialog()
        }

        btnGununYemegi.setOnClickListener {
            val mainAct = (activity as MainActivity)
            mainAct.openFragment(FragmentGununYemegi())
        }

        imgBtnListen.setOnClickListener {

            val foodDate = txtFoodDate.text.toString()
            val noonMenu = txtNoonMenu.text.toString()
            val eveningMenu = txtEveningMenu.text.toString()

            val speechText =
                "$foodDate tarihindeki öğle yemeği menüsünde: $noonMenu var. Akşam yemeğinde ise: $eveningMenu var. Afiyet olsun."

            if (tts.isSpeaking){
                tts.stop()
                imgBtnListen.setImageResource(R.drawable.volume_up)
            }
            else {
                tts.speak(speechText, TextToSpeech.QUEUE_FLUSH, null)
                imgBtnListen.setImageResource(R.drawable.stop)
            }

        }
    }

    private fun showDatePickerDialog() {
        if (foodList.size > 0) {

            if (tts.isSpeaking){
                tts.stop()
                imgBtnListen.setImageResource(R.drawable.volume_up)
            }

            val calendar = Calendar.getInstance()

            val dateTimePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->

                    val realMonth = month + 1

                    if (realMonth.toString().length == 1) {
                        if (dayOfMonth.toString().length == 1)
                            GetSelectedDayMenu("$year-0$realMonth-0$dayOfMonth")
                        else
                            GetSelectedDayMenu("$year-0$realMonth-$dayOfMonth")
                    } else {
                        if (dayOfMonth.toString().length == 1)
                            GetSelectedDayMenu("$year-$realMonth-0$dayOfMonth")
                        else
                            GetSelectedDayMenu("$year-$realMonth-$dayOfMonth")
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            val maxDate = Calendar.getInstance()
            val minDate = Calendar.getInstance()

            maxDate.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            )
            minDate.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
            )

            dateTimePicker.datePicker.maxDate = maxDate.timeInMillis
            dateTimePicker.datePicker.minDate = minDate.timeInMillis
            dateTimePicker.show()
        }
    }

    inner class WebScratch : AsyncTask<Void, Void, Void>() {
        override fun doInBackground(vararg params: Void?): Void? {
            try {
                /*
                Verilerin bulunduğu class isimleri

                Yemek Tarihleri: views-field-field-yemek-tarihi
                Çorbalar: views-field-field-ccorba
                Ana Yemekler: views-field-field-anaa-yemek
                Vejeteryan Yemekler: views-field-field-vejetarien
                Yardımcı Yemekler: views-field-field-yardimciyemek
                Aperatifler: views-field-field-aperatiff
                */

                val doc = Jsoup.connect(SITE_URL).get()
                elDates = doc.select(".views-field-field-yemek-tarihi")
                elSoups = doc.select(".views-field-field-ccorba")
                elMains = doc.select(".views-field-field-anaa-yemek")
                elVegetarians = doc.select(".views-field-field-vejetarien")
                elAuxiliaries = doc.select(".views-field-field-yardimciyemek")
                elSnacks = doc.select(".views-field-field-aperatiff")
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
            }

            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)

            val dates = GetDates() // yyyy-mm-dd

            val noonSoups = GetFood(0, elSoups)
            val eveningSoups = GetFood(1, elSoups)

            val noonMains = GetFood(0, elMains)
            val eveningMains = GetFood(1, elMains)

            val noonVegetrians = GetFood(0, elVegetarians)
            val eveningVegetarians = GetFood(1, elVegetarians)

            val noonAuxiliaries = GetFood(0, elAuxiliaries)
            val eveningAuxiliaries = GetFood(1, elAuxiliaries)

            val noonSnacks = GetFood(0, elSnacks)
            val eveningSnacks = GetFood(1, elSnacks)

            var i = 0

            while (i < dates.size) {

                foodList.add(
                    Food(
                        dates[i],
                        0,
                        noonSoups[i],
                        noonMains[i],
                        noonVegetrians[i],
                        noonAuxiliaries[i],
                        noonSnacks[i]
                    )
                )
                foodList.add(
                    Food(
                        dates[i],
                        1,
                        eveningSoups[i],
                        eveningMains[i],
                        eveningVegetarians[i],
                        eveningAuxiliaries[i],
                        eveningSnacks[i]
                    )
                )

                i++
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val currentDate = sdf.format(Date())

            dateToGettingMenu = currentDate

            GetSelectedDayMenu(dateToGettingMenu)
        }
    }

    private fun GetSelectedDayMenu(date: String) {
        for (food in foodList) {

            val dateOfFood = food.food_date

            if (dateOfFood == date) {

                var actualDate = dateOfFood.substring(dateOfFood.length - 2) + " "

                val month = dateOfFood.substring(5, 7)

                actualDate += when (month) {
                    "01" -> "Ocak"
                    "02" -> "Şubat"
                    "03" -> "Mart"
                    "04" -> "Nisan"
                    "05" -> "Mayıs"
                    "06" -> "Haziran"
                    "07" -> "Temmuz"
                    "08" -> "Ağustos"
                    "09" -> "Eylül"
                    "10" -> "Ekim"
                    "11" -> "Kasım"
                    else -> "Aralık"
                }

                actualDate += " " + dateOfFood.substring(0, 4)

                txtFoodDate.text = actualDate

                if (food.food_time_index == 0) {
                    txtNoonMenu.text =
                        "${food.soup}\n${food.main}\n${food.vegetarian}\n${food.auxiliary}\n${food.snack}"
                } else
                    txtEveningMenu.text =
                        "${food.soup}\n${food.main}\n${food.vegetarian}\n${food.auxiliary}\n${food.snack}"
            }
        }
    }

    private fun GetDates(): ArrayList<String> {

        var datesString = elDates.toString()

        val items = ArrayList<String>()

        val firstValueForSearcing =
            "<span class=\"date-display-single\" property=\"dc:date\" datatype=\"xsd:dateTime\" content=\""
        val secondValueForSearcing = "T00:00:00+03:00\">"

        while (true) {

            if (datesString.indexOf(firstValueForSearcing) == -1 || datesString.indexOf(
                    secondValueForSearcing
                ) == -1
            )
                break

            var text = datesString.substring(
                datesString.indexOf(firstValueForSearcing),
                datesString.indexOf(secondValueForSearcing)
            )
            text = text.substring(firstValueForSearcing.length)

            items.add(text)

            datesString =
                datesString.substring(datesString.indexOf(secondValueForSearcing) + secondValueForSearcing.length)
        }

        val tempList = ArrayList<String>()

        var i = 0

        while (i < items.size) {

            if (!tempList.contains(items[i]))
                tempList.add(items[i])

            i++
        }

        return tempList

    }

    private fun GetFood(dayIndex: Int, elementToGetting: Elements): ArrayList<String> {

        if (dayIndex < 0 || dayIndex > 1)
            return arrayListOf("null")

        var elementString = elementToGetting.toString()

        val items = ArrayList<String>()

        val firstValueForSearcing = "<a href=\""
        val secondValueForSearcing = "</a>"

        while (true) {

            if (elementString.indexOf(firstValueForSearcing) == -1)
                break

            var text = elementString.substring(
                elementString.indexOf(firstValueForSearcing),
                elementString.indexOf(secondValueForSearcing)
            )

            if (elementString.substring(
                    elementString.indexOf(firstValueForSearcing),
                    elementString.indexOf(secondValueForSearcing) + (secondValueForSearcing.length + 1)
                ).indexOf(",") != -1
            ) {
                text = text.substring(firstValueForSearcing.length)
                text = text.substring(text.indexOf(">") + 1)

                text = "$text,"
            } else {
                text = text.substring(firstValueForSearcing.length)
                text = text.substring(text.indexOf(">") + 1)
            }

            items.add(text)

            elementString =
                elementString.substring(elementString.indexOf(secondValueForSearcing) + secondValueForSearcing.length)
        }

        val newList = ArrayList<String>()

        var j = 0

        while (j < items.size) {

            if (items[j].endsWith(",")) {
                if (items[j + 1].endsWith(",")) {
                    if (items[j + 2].endsWith(",")) {
                        newList.add(items[j] + items[j + 1] + items[j + 2] + items[j + 3])

                        j += 4
                    } else {
                        newList.add(items[j] + items[j + 1] + items[j + 2])
                    }

                    j += 3
                } else {
                    newList.add(items[j] + items[j + 1])

                    j += 2
                }
            } else {
                newList.add(items[j])
                j++
            }
        }

        val tempList = ArrayList<String>()

        var k = dayIndex

        while (k < newList.size) {
            tempList.add(newList[k])

            k += 2
        }

        return tempList
    }
}