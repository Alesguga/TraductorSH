package net.azarquiel.traductorshfiltroclase

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import net.azarquiel.recyclerviewWords.adapter.CustomAdapter
import net.azarquiel.traductorshfiltroclase.databinding.ActivityMainBinding
import net.azarquiel.traductorshfiltroclase.model.Word
import net.azarquiel.traductorshfiltroclase.util.Util
import java.util.Locale

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, CustomAdapter.OnClickListenerRecycler, TextToSpeech.OnInitListener  {
    // Singleton TTS
    companion object {
        private var tts: TextToSpeech? = null
    }

    private lateinit var adapter: CustomAdapter
    private lateinit var words: java.util.ArrayList<Word>
    private lateinit var inglesSH: SharedPreferences
    private lateinit var espanolSH: SharedPreferences
    private lateinit var searchView: SearchView
    private lateinit var binding: ActivityMainBinding
    private var isSpFlag = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        tts = TextToSpeech(this, this)
        Util.inyecta(this, "espanol.xml")
        Util.inyecta(this, "ingles.xml")
        espanolSH = getSharedPreferences("espanol", Context.MODE_PRIVATE)
        inglesSH = getSharedPreferences("ingles", Context.MODE_PRIVATE)
        initRV()
        getAllWord()
        adapter.setWords(words)
    }

    private fun initRV() {
        adapter = CustomAdapter(this, R.layout.rowword, this)
        binding.cm.rvpalabras.adapter = adapter
        binding.cm.rvpalabras.layoutManager = LinearLayoutManager(this)
    }

    private fun getAllWord() {
        words = ArrayList<Word>()
        var inglesAll = inglesSH.all
        for ((key, value) in inglesAll) {
            var wordsp = espanolSH.getString(key, null)
            var word = Word(key, wordsp!!, value.toString())
            words.add(word)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        // ************* <Filtro> ************
        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.setQueryHint("Search...")
        searchView.setOnQueryTextListener(this)
        // ************* </Filtro> ************

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_flag -> {
                if (isSpFlag)
                    item.setIcon(R.drawable.flagi)
                else
                    item.setIcon(R.drawable.flage)
                isSpFlag= !isSpFlag
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // ************* <Filtro> ************
    override fun onQueryTextChange(query: String): Boolean {
        val original = ArrayList<Word>(words)
        if (isSpFlag) {
            adapter.setWords(original.filter { palabra -> palabra.spWord.contains(query,true) })
        }
        else {
            adapter.setWords(original.filter { palabra -> palabra.enWord.contains(query,true) })
        }
        return false
    }

    override fun onQueryTextSubmit(text: String): Boolean {
        return false
    }

    // ************* </Filtro> ************

    private fun speakOutSp(palabra: String) {
        tts!!.language = Locale("es")
        tts!!.speak(palabra, TextToSpeech.QUEUE_ADD, null,"")
    }
    private fun speakOutEn(palabra: String) {
        tts!!.language = Locale.US
        tts!!.speak(palabra, TextToSpeech.QUEUE_ADD, null,"")
    }


    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            var result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Languaje us no suportado")
            } else {
            }
            result = tts!!.setLanguage(Locale("es"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Languaje spa no suportado")
            } else {
            }
        } else {
            Log.e("TTS", "Fallo en inicialización de TTS")
        }
    }

    override fun onClickSp(itemView: View) {
        val word = itemView.tag as Word
        speakOutSp(word.spWord)
    }

    override fun onClickEn(itemView: View) {
        val word = itemView.tag as Word
        speakOutEn(word.enWord)
    }
}