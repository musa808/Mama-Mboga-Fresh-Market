package com.example.mamamboga

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

data class Vegetable(
    val name: String,
    val emoji: String,
    val price: Int,
    var quantity: Int = 0
)

class MainActivity : AppCompatActivity() {

    private lateinit var vegetables: ArrayList<Vegetable>
    private lateinit var adapter: VegetableAdapter
    private lateinit var txtCartTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtCartTotal = findViewById(R.id.txtCartTotal)

        vegetables = arrayListOf(
            Vegetable("Sukuma Wiki", "🥬", 30),
            Vegetable("Tomatoes", "🍅", 80),
            Vegetable("Onions", "🧅", 100),
            Vegetable("Carrots", "🥕", 60),
            Vegetable("Cabbage", "🥦", 70),
            Vegetable("Potatoes", "🥔", 120),
            Vegetable("Spinach", "🌿", 40),
            Vegetable("Green Pepper", "🫑", 20)
        )

        val listView = findViewById<ListView>(
            R.id.listVegetables
        )

        adapter = VegetableAdapter()
        listView.adapter = adapter

        findViewById<Button>(R.id.btnWhatsApp)
            .setOnClickListener {
                sendWhatsAppOrder()
            }

        updateCart()
    }

    private fun updateCart() {
        val count = vegetables.sumOf { it.quantity }
        val total = vegetables.sumOf {
            it.price * it.quantity
        }

        txtCartTotal.text =
            "Cart: $count items | Total: KSh $total"
    }

    private fun sendWhatsAppOrder() {
        val selected = vegetables.filter {
            it.quantity > 0
        }

        if (selected.isEmpty()) {
            Toast.makeText(
                this,
                "Please add vegetables to your cart first.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val order = StringBuilder()
        order.append("Hello Mama Mboga! My order:\n\n")

        selected.forEach {
            order.append(
                "${it.name} x ${it.quantity} = " +
                        "KSh ${it.price * it.quantity}\n"
            )
        }

        val total = vegetables.sumOf {
            it.price * it.quantity
        }

        order.append("\nTotal: KSh $total")

        val url = "https://wa.me/?text=" +
                Uri.encode(order.toString())

        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        )
    }

    private inner class VegetableAdapter : BaseAdapter() {

        override fun getCount() = vegetables.size

        override fun getItem(position: Int) =
            vegetables[position]

        override fun getItemId(position: Int) =
            position.toLong()

        override fun getView(
            position: Int,
            convertView: View?,
            parent: ViewGroup
        ): View {

            val view = convertView ?: LayoutInflater.from(
                this@MainActivity
            ).inflate(
                R.layout.item_vegetable,
                parent,
                false
            )

            val vegetable = vegetables[position]

            view.findViewById<TextView>(
                R.id.txtEmoji
            ).text = vegetable.emoji

            view.findViewById<TextView>(
                R.id.txtName
            ).text = vegetable.name

            view.findViewById<TextView>(
                R.id.txtPrice
            ).text = "KSh ${vegetable.price} per unit"

            view.findViewById<Button>(
                R.id.btnAdd
            ).setOnClickListener {
                vegetable.quantity++
                updateCart()
                Toast.makeText(
                    this@MainActivity,
                    "${vegetable.name} added to cart",
                    Toast.LENGTH_SHORT
                ).show()
            }

            return view
        }
    }
}