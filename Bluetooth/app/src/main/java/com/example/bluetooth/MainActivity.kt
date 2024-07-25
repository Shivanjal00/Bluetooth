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

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_ENABLE_BT : Int = 1
    private val REQUEST_CODE_DISCOVERABLE_BT : Int = 2

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
            bluetoothStatusTv.setText("Bluetooth is not available")
        }else{
            bluetoothStatusTv.setText("Bluetooth is  available")
        }

        if(BAdpter.isEnabled){
            bluetoothTv.setImageResource(R.drawable.baseline_bluetooth_24)

        }else{
            bluetoothTv.setImageResource(R.drawable.baseline_bluetooth_disabled_24)
        }

        turnOnButton.setOnClickListener {
            if(BAdpter.isEnabled){
                Toast.makeText(this@MainActivity,"Already On",Toast.LENGTH_LONG).show()
            }else{

                val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@setOnClickListener
                }
                startActivityForResult(intent,REQUEST_CODE_ENABLE_BT)
            }
        }


        turnOffButton.setOnClickListener {

            if(!BAdpter.isEnabled){
                Toast.makeText(this@MainActivity,"Already Off",Toast.LENGTH_LONG).show()
            }else{

                BAdpter.disable()
                bluetoothTv.setImageResource(R.drawable.baseline_bluetooth_disabled_24)
                Toast.makeText(this@MainActivity,"Bluetooth turned Off",Toast.LENGTH_LONG).show()


            }

        }



        discoverableButton.setOnClickListener {

            if (!BAdpter.isDiscovering){
                Toast.makeText(this@MainActivity,"Making your device discoverable",Toast.LENGTH_LONG).show()
                val intent = Intent(Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE))
                startActivityForResult(intent,REQUEST_CODE_DISCOVERABLE_BT)
            }
        }

        pairedButton.setOnClickListener {
            if (BAdpter.isEnabled){
                pairedTv.setText("Paired devices")
                val devices = BAdpter.bondedDevices
                for (device in devices){
                    val deviceName = device.name
                    val deviceAddress = device
                    pairedTv.append("\nDevice: $deviceName , $device")
                }

            }else{
                Toast.makeText(this@MainActivity,"Turn on bluetooth first",Toast.LENGTH_LONG).show()

            }
        }




    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            REQUEST_CODE_ENABLE_BT->
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(this@MainActivity,"Bluetooth On",Toast.LENGTH_LONG).show()
                    bluetoothTv.setImageResource(R.drawable.baseline_bluetooth_24)
                }else{
                    Toast.makeText(this@MainActivity,"Could not on bluetooth",Toast.LENGTH_LONG).show()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}