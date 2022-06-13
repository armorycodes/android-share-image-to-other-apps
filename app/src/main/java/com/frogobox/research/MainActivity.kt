package com.frogobox.research

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.frogobox.research.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnSendWa.setOnClickListener {
            val title = "Share To"
            val message = "This SS Send From ${this.packageName}"
            Utils.sendSStoWhatsapp(this, binding.container, title, message)
        }
    }

}