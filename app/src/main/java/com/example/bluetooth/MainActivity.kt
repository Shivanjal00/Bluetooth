package com.example.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_ENABLE_BT : Int = 1
    private val REQUEST_CODE_DISCOVERABLE_BT : Int = 2
    private val REQUEST_CODE_BLUETOOTH_CONNECT : Int = 3
    private val REQUEST_CODE_BLUETOOTH_SCAN : Int = 4

    lateinit var BAdpter : BluetoothAdapter
    lateinit var bluetoothStatusTv :TextView
    lateinit var pairedTv :TextView
    lateinit var bluetoothTv :ImageView
    lateinit var turnOnButton :Button
    lateinit var turnOffButton :Button
    lateinit var discoverableButton :Button
    lateinit var pairedButton :Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bluetoothStatusTv = findViewById(R.id.bluetoothStatusTv)
        pairedTv = findViewById(R.id.pairedTv)
        bluetoothTv = findViewById(R.id.bluetoothTv)
        turnOnButton = findViewById(R.id.turnOnButton)
        turnOffButton = findViewById(R.id.turnOffButton)
        discoverableButton = findViewById(R.id.discoverableButton)
        pairedButton = findViewById(R.id.pairedButton)

        BAdpter = BluetoothAdapter.getDefaultAdapter()

        if(BAdpter == null){
            bluetoothStatusTv.text = "Bluetooth is not available"
        }else{
            bluetoothStatusTv.text = "Bluetooth is available"
        }

        if(BAdpter.isEnabled){
            bluetoothTv.setImageResource(R.drawable.baseline_bluetooth_24)
        }else{
            bluetoothTv.setImageResource(R.drawable.baseline_bluetooth_disabled_24)
        }

        turnOnButton.setOnClickListener {
            if(BAdpter.isEnabled){
                Toast.makeText(this@MainActivity, "Already On", Toast.LENGTH_LONG).show()
            }else{
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_CODE_BLUETOOTH_CONNECT)
                } else {
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(intent, REQUEST_CODE_ENABLE_BT)
                }
            }
        }

        turnOffButton.setOnClickListener {
            if (!BAdpter.isEnabled){
                Toast.makeText(this@MainActivity, "Already Off", Toast.LENGTH_LONG).show()
            }else{
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_CODE_BLUETOOTH_CONNECT)
                } else {
                    BAdpter.disable()
                    bluetoothTv.setImageResource(R.drawable.baseline_bluetooth_disabled_24)
                    Toast.makeText(this@MainActivity, "Bluetooth turned Off", Toast.LENGTH_LONG).show()
                }
            }
        }

        discoverableButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_SCAN), REQUEST_CODE_BLUETOOTH_SCAN)
            } else {
                if (!BAdpter.isDiscovering){
                    Toast.makeText(this@MainActivity, "Making your device discoverable", Toast.LENGTH_LONG).show()
                    val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                    startActivityForResult(intent, REQUEST_CODE_DISCOVERABLE_BT)
                }
            }
        }

        pairedButton.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_CODE_BLUETOOTH_CONNECT)
            } else {
                if (BAdpter.isEnabled){
                    pairedTv.text = "Paired devices"
                    val devices = BAdpter.bondedDevices
                    for (device in devices){
                        val deviceName = device.name
                        val deviceAddress = device.address
                        pairedTv.append("\nDevice: $deviceName , $deviceAddress")
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Turn on bluetooth first", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_CODE_ENABLE_BT->
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(this@MainActivity, "Bluetooth On", Toast.LENGTH_LONG).show()
                    bluetoothTv.setImageResource(R.drawable.baseline_bluetooth_24)
                }else{
                    Toast.makeText(this@MainActivity, "Could not turn on bluetooth", Toast.LENGTH_LONG).show()
                }
            REQUEST_CODE_DISCOVERABLE_BT->
                if(resultCode == Activity.RESULT_CANCELED){
                    Toast.makeText(this@MainActivity, "Discoverability cancelled", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@MainActivity, "Discoverability enabled", Toast.LENGTH_LONG).show()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_BLUETOOTH_CONNECT, REQUEST_CODE_BLUETOOTH_SCAN -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with Bluetooth operations
                    if (turnOnButton.isPressed) {
                        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        if (ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return
                        }
                        startActivityForResult(intent, REQUEST_CODE_ENABLE_BT)
                    } else if (turnOffButton.isPressed) {
                        BAdpter.disable()
                        bluetoothTv.setImageResource(R.drawable.baseline_bluetooth_disabled_24)
                        Toast.makeText(this@MainActivity, "Bluetooth turned Off", Toast.LENGTH_LONG).show()
                    } else if (discoverableButton.isPressed) {
                        val intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                        startActivityForResult(intent, REQUEST_CODE_DISCOVERABLE_BT)
                    } else if (pairedButton.isPressed) {
                        pairedTv.text = "Paired devices"
                        val devices = BAdpter.bondedDevices
                        for (device in devices) {
                            val deviceName = device.name
                            val deviceAddress = device.address
                            pairedTv.append("\nDevice: $deviceName , $deviceAddress")
                        }
                    }
                } else {
                    // Permission denied, display error message
                    Toast.makeText(this@MainActivity, "Bluetooth permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
