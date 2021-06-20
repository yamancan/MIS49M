package com.boun.bounyemekhane.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.room.Room
import com.boun.bounyemekhane.R
import com.boun.bounyemekhane.dao.RecipeDao
import com.boun.bounyemekhane.database.RecipeDatabase
import java.text.SimpleDateFormat
import java.util.*

class FragmentGununYemegi : Fragment() {

    private lateinit var txtTodaysRecipeName: TextView
    private lateinit var txtRecipe: TextView
    private lateinit var tts: TextToSpeech
    private lateinit var linearChangeDay: LinearLayout
    private lateinit var btnChangeDay: Button
    private lateinit var btnBounYemekhane: Button
    private lateinit var imgBtnListen: ImageView

    private val SHARED_PREF_FILE = "shared_pref_file_name"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gunun_yemegi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addRecipesToDatabase()
        initializeVariables()
        onClickListener()
    }

    override fun onPause() {
        super.onPause()

        if (tts.isSpeaking) {
            tts.stop()
            imgBtnListen.setImageResource(R.drawable.volume_up)
        }
    }

    private fun initializeVariables() {
        txtTodaysRecipeName = requireView().findViewById(R.id.txtTodaysRecipeName)
        txtRecipe = requireView().findViewById(R.id.txtRecipe)
        imgBtnListen = requireView().findViewById(R.id.imgBtnListen)
        linearChangeDay = requireView().findViewById(R.id.linearChangeDay)
        btnChangeDay = requireView().findViewById(R.id.btnChangeDay)
        btnBounYemekhane = requireView().findViewById(R.id.btnBounYemekhane)
        imgBtnListen = requireView().findViewById(R.id.imgBtnListen)
        tts = TextToSpeech(requireContext()) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale("tr_TR")
            }
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())

        GetDailyRecipeAndSetTextViews(
            currentDate.substring(8, 10).toInt(),
            currentDate.substring(5, 7).toInt(),
            currentDate.substring(0, 4).toInt()
        )
    }

    private fun onClickListener() {
        linearChangeDay.setOnClickListener {
            showDatePickerDialog()
        }

        btnChangeDay.setOnClickListener {
            showDatePickerDialog()
        }

        btnBounYemekhane.setOnClickListener {
            val mainAct = (activity as MainActivity)
            mainAct.openFragment(FragmentBounYemekhane())
        }

        imgBtnListen.setOnClickListener {
            val recipeName = txtTodaysRecipeName.text.toString()
            val todaysRecipe = txtRecipe.text.toString()

            val speechText =
                "Günün yemek tarifi: $recipeName. $todaysRecipe"

            if (tts.isSpeaking) {
                tts.stop()
                imgBtnListen.setImageResource(R.drawable.volume_up)
            } else {
                tts.speak(speechText, TextToSpeech.QUEUE_FLUSH, null)
                imgBtnListen.setImageResource(R.drawable.stop)
            }
        }
    }

    private fun addRecipesToDatabase(){

        val sharedPref = requireActivity().getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE)

        if (!sharedPref.getBoolean("anyValueInDB", false)){
            GetRecipeDao().AddRecipe(0,"Terbiyeli Yoğurt Çorbası", "Malzemeler:\n" +
                    "6 su bardağı su(ya da et suyu)\n" +
                    "Terbiyesi için:\n" +
                    "2 yemek kaşığı un\n" +
                    "1 adet yumurta\n" +
                    "2 çay bardağı süzme yoğurt\n" +
                    "1/2 su bardağı su\n" +
                    "1 çay kaşığı tuz\n" +
                    "Servisi için:\n" +
                    "1 yemek kaşığı tereyağı\n" +
                    "1 çay kaşığı kırmızı biber\n" +
                    "1 tatlı kaşığı nane\nNasıl Yapılır?\n" +
                    "Derin bir tencerede suyu kaynatmaya başlayın. Su kaynayana dek terbiyesini hazırlamak üzere terbiye için gerekli tüm malzemeleri bir kapta çırpıcıyla karıştırın. Su kaynamaya başlayınca yumurtanın kesilmemesi için hazırladığınız terbiye karışımına kaynayan sudan azar azar ekleyip hızla karıştırın. Tüm terbiye karışımını çorbaya ekleyip ara ara karıştırarak yaklaşık 15 dakika pişirin. O pişerken bir sos tavasına sosu için gerekli tereyağını alıp kızdırın, toz biber ve naneyi de ekleyip ocaktan alın. Kızdırılmış sosu çorbanın üzerine gezdirip karıştırarak servis edin. Afiyet olsun.")

            GetRecipeDao().AddRecipe(1, "Şiş Köfte", "Malzemeler:\n" +
                    "300 gram dana kıyma\n" +
                    "1 adet soğan(rendelenmiş, suyu sıkılmış)\n" +
                    "1 adet yumurta\n" +
                    "3 yemek kaşığı ekmek kırıntısı\n" +
                    "1/2 demet doğranmış maydanoz\n" +
                    "1,5 çay kaşığı tuz(köfte için)\n" +
                    "1 çay kaşığı kimyon\n" +
                    "1 diş rendelenmiş sarımsak\n" +
                    "1/2 çay kaşığı karabiber\n" +
                    "1 adet küçük boy pide(küp doğranmış)\n" +
                    "50 gram eritilmiş tereyağı\n" +
                    "3 yemek kaşığı domates püresi\n" +
                    "1/2 çay kaşığı tuz(pide için)\n" +
                    "4 yemek kaşığı çırpılmış yoğurt\n" +
                    "Evi bir anda en ala kebapçıya dönüştürecek bir tarif var sırada. Üstelik tost makinesinde pişiyor! Nasıl mı? Detaylar için sizi hemen aşağıya doğru alalım.\n" +
                    "\n" +
                    "Nasıl Yapılır?\n" +
                    "Bir karıştırma kabına kıyma, soğan, yumurta, ekmek kırıntısı, maydanoz, tuz, kimyon, sarımsak ve karabiberi aktarıp güzelce yoğurun. Ellerinizi ıslatarak hazırladığınız kıymalı harcı şişlere dikkatle sarın. Hazırladığınız şişleri tost makinesine dizip makinenin kapağının tam olarak kapanmaması için arasına bir mutfak gereci koyun ve yaklaşık 10-12 dakika bu şekilde pişirin. O esnada pide, tereyağı, domates püresi ve tuzu karıştırın. Şiş köfteleri tost makinesinden aldıktan sonra pideleri koyup 5 dakika kadar pişirin. Servis tabağına önce pideleri, ardından yoğurdu, en üste de köfteleri koyun. İşte bu kadar! Ellerinize sağlık.")

            GetRecipeDao().AddRecipe(2, "Nar Ekşili Patlıcan Salatası", "Malzemeler:\n" +
                    "2 su bardağı közlenmiş patlıcan\n" +
                    "1 su bardağı ceviz içi\n" +
                    "2 adet taze soğan\n" +
                    "1/2 çay bardağı zeytinyağı\n" +
                    "1/2 adet limonun suyu\n" +
                    "1 çay kaşığı tuz\n" +
                    "1/2 çay bardağı nar ekşisi\n" +
                    "Ana yemek olarak hazırladığımız şiş köftelerin yanına en çok yakışacak salatada sıra! Patlıcan ve köftenin uyumuna kim hayır diyebilir ki...\n" +
                    "\n" +
                    "Nasıl Yapılır?\n" +
                    "Patlıcanları, taze soğanları ve cevizi güzelce kıyıp bir tavaya zeytinyağı aktarın ve kıydığınız patlıcan, taze soğan ve cevizi yüksek ateşte 3-4 dakika soteleyin. Üzerine tuz, limon suyu ve nar ekşisi eklediğiniz karışımı sıcak olarak servis edin. Üzerine kıyılmış maydanozlar serpiştirebilirsiniz. Afiyet olsun.")

            GetRecipeDao().AddRecipe(3, "Böğürtlen Soslu Panna Cotta","Malzemeler:\n" +
                    "250 ml. krema\n" +
                    "250 ml. soğuk süt\n" +
                    "1 paket vanilya(ya da vanilya çubuğu)\n" +
                    "3 yemek kaşığı toz şeker\n" +
                    "3 yaprak jelatin\n" +
                    "Böğürtlen sosu İçin:\n" +
                    "1 su bardağı böğürtlen\n" +
                    "5 yemek kaşığı şeker\n" +
                    "1 tatlı kaşığı buğday nişastası\n" +
                    "1/2 su bardağı su\n" +
                    "Sıcak havalarda tatlı hakkını ağır olmayan sütlü tatlılardan yana kullananları hemen böyle alalım. Bugün sofranın yıldızı belli!\n" +
                    "\n" +
                    "Nasıl Yapılır?\n" +
                    "Yumuşaması için jelatinleri soğuk su dolu bir kasede yaklaşık 5 dakika bekletin. Bir tencereye krema, süt, toz şeker ve vanilyayı aktarıp kısık ateşte pişirmeye başlayın ve kaynama derecesine gelince ocaktan alın. Suda yumuşayan jelatinlerin fazla suyunu sıkıp kremalı sütlü karışımın içine ilave edin ve karıştırarak jelatinlerin tamamen erimesini sağlayın. Silikon ya da tek kullanımlık kapları ıslatıp karışımı bu kaplara eşit olarak pay edin. Buzdolabında 2 saat kadar bekletin. Sosu için böğürtlenleri, şekeri ve suyu bir tencereye alıp 5 dakika kaynatarak pişirin, ardından süzün. Nişastayı 2 yemek kaşığı kadar soğuk suda karıştırıp süzdüğünüz böğürtlen sosa ekleyin ve kısık ateşte hafif koyulaşana dek pişirin ve soğumaya bırakın. Servis zamanı gelince tatlıları tabaklara ters çevirerek çıkarın ve hazırladığınız sosla süsleyerek servis edin. Afiyet olsun.")

            GetRecipeDao().AddRecipe(4,"Fırında Tavuk Şiş", "Malzemeler\n" +
                    "\n" +
                    "500 gram kemiksiz kuşbaşı tavuk göğsü\n" +
                    "\n" +
                    "1 adet büyük boy soğanın suyu\n" +
                    "\n" +
                    "2 yemek kaşığı yoğurt\n" +
                    "\n" +
                    "3 diş sarımsak\n" +
                    "\n" +
                    "1,5 kahve fincanı zeytinyağı\n" +
                    "\n" +
                    "1 tatlı kaşığı tuz\n" +
                    "\n" +
                    "2 çay kaşığı karabiber\n" +
                    "\n" +
                    "1 çay kaşığı toz kırmızı biber\n" +
                    "\n" +
                    "1 çay kaşığı kuru kekik\n" +
                    "\n" +
                    "1 dal biberiye (arzuya göre)\n" +
                    "\n" +
                    "Araları için:\n" +
                    "\n" +
                    "Arzu ettiğiniz sebzeleri ekleyebilirsiniz\n" +
                    "\n" +
                    "Fırında tavuk şiş nasıl yapılır?\n" +
                    "Kuşbaşı tavukları bir karıştırma kabına alın ve üzerine zeytinyağı, yoğurt, soğan suyu, rendelenmiş sarımsakları, tuz ve baharatları ekleyip güzelce harmanlayın. Buzdolabında 1 saat, eğer bir sonraki gün için yapacaksanız 1 gece bekletin. Sebzeleri de orta boyutta olacak şekilde doğrayıp üzerine zeytinyağı, tuz ve karabiber ilave edin ve harmanlayın. Tavuk ve sebzeleri sırayla çöp şişlere dizin. Fırın tepsisinin üzerine fırın telini yerleştirin. Fırını 200 derecede 5 dakika kadar ısıtın. Ardından fırının kapağını açıp hazırladığınız çöp şişleri telin üzerine yerleştirin. Yaklaşık 20 dakika pişirin.")

            GetRecipeDao().AddRecipe(5,"Roka Salatası","Malzemeler\nYarım demet roka\n" +
                    "\n" +
                    "2 yaprak marul\n" +
                    "\n" +
                    "3 dal nane\n" +
                    "\n" +
                    "2 küçük domates\n" +
                    "\n" +
                    "2 adet yeşil soğan\n" +
                    "\n" +
                    "Zeytinyağı\n" +
                    "\n" +
                    "Limon\n" +
                    "\n" +
                    "Tuz\n" +
                    "\n" +
                    "Roka salatası nasıl yapılır?\n" +
                    "Yeşillikleri sirkeli suda bekletip güzelce yıkadıktan sonra tüm yeşillikleri (marul, nane ve roka) doğrayın. Soğanı ince ince kıyıp salataya ekleyin. Üzerine küçük çeri domatesleri ekleyin. Limon, zeytinyağı ve tuz ayrı bir kapta iyice karıştırıp salataya ekleyebilirsiniz.")

            GetRecipeDao().AddRecipe(6,"Kadayıflı Muhallebi Tatlısı","Malzemeler\n" +
                    "\n" +
                    "250 gram kadayıf\n" +
                    "\n" +
                    "5 yemek kaşığı toz şeker\n" +
                    "\n" +
                    "4 yemek kaşığı tereyağı\n" +
                    "\n" +
                    "Bir su bardağı iri dövülmüş ceviz\n" +
                    "\n" +
                    "Muhallebi için:\n" +
                    "\n" +
                    "1 litre süt\n" +
                    "\n" +
                    "Yarım su bardağı toz şeker\n" +
                    "\n" +
                    "2 yemek kaşığı mısır nişastası\n" +
                    "\n" +
                    "2 yemek kaşığı un\n" +
                    "\n" +
                    "1 yumurta\n" +
                    "\n" +
                    "1 paket vanilya\n" +
                    "\n" +
                    "1 paket krem şanti\n" +
                    "\n" +
                    "Kadayıflı Muhallebi Tatlısı nasıl yapılır?\n" +
                    "Tereyağını geniş bir tavada eritin. Kadayıflarınızı şekerle birlikte tereyağında ara sıra karıştırarak kısış ateşte kavurun. İçine dövülmüş cevizinizi ekleyip altını kapatın. Başka bir kaba alıp soğumaya bırakın.\n" +
                    "\n" +
                    "Bir sos tenceresinde un, nişasta, vanilya, şeker ve yumurtayı karıştırın. Sütü de ekleyip pişirmeye başlayın. Muhallebi kıvamını alınca altını kapatıp soğumasını bekleyin. Soğuyunca içine krem şantiyi ilave edip mikserle çırpın.\n" +
                    "\n" +
                    "Kadayıflarınızın yarısını dikdörtgen tepsinizin tabanına iyice yayın. Üzerine muhallebinin tamamını her tarafı eşit olacak şekilde yayın. En üste kadayıfların kalanını döküp yine her tarafı eşit olacak şekilde iyice yayın. En az 2 saat buzdolabında katılaşması için bekletin. Ardından dilimleyip servis edebilirsiniz.")

            GetRecipeDao().AddRecipe(7,"Cacıklı Arap Köftesi","Köftesi İçin Malzeme\n" +
                    "2 su bardağı köftelik bulgur\n" +
                    "200 gr kıyma\n" +
                    "1 soğan\n" +
                    "2 su bardağı un\n" +
                    "1 tatlı kaşığı tuz\n" +
                    "1çay kaşığı karabiber,toz kırmızı biber\n" +
                    "Ilık su bulguru ıslamak için\n" +
                    "Cacık Malzemesi\n" +
                    "500gr sarımsaklı yoğurt\n" +
                    "1/2 kilo ıspanak veya pazı\n" +
                    "1 soğan\n" +
                    "2 yemek kaşığı tereyağ\n" +
                    "1 yemek kaşığı un\n" +
                    "tuz,karabiber,acı pulbiber\n" +
                    "Sosu İçin\n" +
                    "50 gr tereyağ\n" +
                    "1 çorba kaşığı pulbiber\nHazırlanışı \n" +
                    "Bulgur ılık bol suda yıkanır.\n" +
                    "Yıkanıp süzülen bulgura bir çay bardağı kadar ılık su ilave edilerek demlenmesi beklenir. \n" +
                    "Cam kase içinde ayıklanıp rendelenen soğan,ılık suda şişen bulgur,kıyma, baharatlar iyice yoğurulur.\n" +
                    "İki su bardağı un katılarak köfte kıvamı yaklanır.(Un miktarı azaltılıp ,çoğalabilir kıvamını tutturmak için) Küçük yuvarlak fındık büyüklüğünde köftecikler yapılır.\n" +
                    "Derin tenceredeki tuzlu su kaynamaya yakın köfteler salınarak kontrollü şekilde beş dakikada haşlanır.\n" +
                    "Bu arada ıspanaklar bol suda temizlenir.\n" +
                    "İnce doğranak suda beş dakika haşlanır,süzgeçe alınır.\n" +
                    "İnce doğaranan soğan,tereyağ,un teflon tavada kavrulur ıspanaklar eklenerek kavurma işlemi tamamlanır.\n" +
                    "Baharatlanan ıspanaklı harç ılınınca sarımsaklı yoğurtla karıştırılarak servis kabına alınır.\n" +
                    "Haşlanan köftelerin hepsi tavada iki yemek kaşığı tereyağ ile hafif kızartılır .\n" +
                    "Kızaran arap köfteleri cacıklı ıspanağın üzerine dökülür.\n" +
                    "Ayrıca tavada eritilen yağ,pulbiber servis öncesi cacıklı arap köftesinin üzerine gezdirilerek ikram edilir.")

            sharedPref.edit().putBoolean("anyValueInDB", true).apply()
        }

    }

    private fun showDatePickerDialog() {
        if (tts.isSpeaking) {
            tts.stop()
            imgBtnListen.setImageResource(R.drawable.volume_up)
        }

        val calendar = Calendar.getInstance()

        val dateTimePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->

                val realMonth = month + 1

                GetDailyRecipeAndSetTextViews(dayOfMonth, realMonth, year)

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        dateTimePicker.show()
    }

    private fun GetDailyRecipeAndSetTextViews(day: Int, month: Int, year: Int) {

        // Bize verilen gün, ay ve yıl değerlerini kullanarak aşağıdaki formülü uyguladık.
        // (TK / 7) + AK + (YK / 7) + (Yıl / 4) / 7 = GününSayısı
        // Bunu yapmamızın amacı günün haftadaki sayısını bulma (pzt = 1, salı = 2 ...)

        val tk = day % 7
        val ak = when (month) {
            1 -> 1
            2 -> 4
            3 -> 4
            4 -> 7
            5 -> 2
            6 -> 5
            7 -> 7
            8 -> 3
            9 -> 6
            10 -> 1
            11 -> 4
            12 -> 6
            else -> 0
        }
        val yk = (year - 2000) % 7

        var dayCount = tk + ak + yk + (((year - 2000) / 4) % 7)

        while (dayCount > 7)
            dayCount -= 7

        val recipe = GetRecipeDao().GetRecipe(dayCount.toByte())

        txtTodaysRecipeName.text = recipe.recipeName
        txtRecipe.text = recipe.recipe

    }

    private fun GetRecipeDao() : RecipeDao{
        val db = Room.databaseBuilder(
            requireContext(),
            RecipeDatabase::class.java,
            "recipe"
        ).allowMainThreadQueries()
            .build()

        return db.recipeDao()
    }
}