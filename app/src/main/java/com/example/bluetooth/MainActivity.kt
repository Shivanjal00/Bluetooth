package com.example.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_ENABLE_BT = 1
    private val REQUEST_CODE_DISCOVERABLE_BT = 2
    private val REQUEST_CODE_BLUETOOTH_CONNECT = 3
    private val REQUEST_CODE_BLUETOOTH_SCAN = 4
    private val REQUEST_CODE_LOCATION_PERMISSION = 5

    lateinit var BAdpter: BluetoothAdapter
    lateinit var bluetoothStatusTv: TextView
    lateinit var pairedTv: TextView
    lateinit var bluetoothTv: ImageView
    lateinit var turnOnButton: Button
    lateinit var turnOffButton: Button
    lateinit var discoverableButton: Button
    lateinit var scanButton: Button
    lateinit var pairedButton: Button
    lateinit var availableDevicesListView: ListView
    lateinit var devicesAdapter: ArrayAdapter<String>
    private val devicesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bluetoothStatusTv = findViewById(R.id.bluetoothStatusTv)
        pairedTv = findViewById(R.id.pairedTv)
        bluetoothTv = findViewById(R.id.bluetoothTv)
        turnOnButton = findViewById(R.id.turnOnButton)
        turnOffButton = findViewById(R.id.turnOffButton)
        discoverableButton = findViewById(R.id.discoverableButton)
        scanButton = findViewById(R.id.ScanButton)
        pairedButton = findViewById(R.id.pairedButton)
        availableDevicesListView = findViewById(R.id.availableDevicesListView)

        devicesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, devicesList)
        availableDevicesListView.adapter = devicesAdapter

        BAdpter = BluetoothAdapter.getDefaultAdapter()

        if (BAdpter == null) {
            bluetoothStatusTv.text = "Bluetooth is not available"
        } else {
            bluetoothStatusTv.text = "Bluetooth is available"
            updateBluetoothStatus()
        }

        turnOnButton.setOnClickListener {
            requestBluetoothConnectPermission {
                enableBluetooth()
            }
        }

        turnOffButton.setOnClickListener {
            requestBluetoothConnectPermission {
                if (BAdpter.isEnabled) {
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
                        return@requestBluetoothConnectPermission
                    }
                    BAdpter.disable()
                    bluetoothTv.setImageResource(R.drawable.baseline_bluetooth_disabled_24)
                    Toast.makeText(this@MainActivity, "Bluetooth turned Off", Toast.LENGTH_LONG).show()
                }
            }
        }

        discoverableButton.setOnClickListener {
            requestBluetoothScanPermission {
                makeDeviceDiscoverable()
            }
        }

        pairedButton.setOnClickListener {
            requestBluetoothConnectPermission {
                if (BAdpter.isEnabled) {
                    pairedTv.text = "Paired devices:"
                    val devices = BAdpter.bondedDevices
                    for (device in devices) {
                        val deviceName = device.name
                        val deviceAddress = device.address
                        pairedTv.append("\nDevice: $deviceName , $deviceAddress")
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Turn on Bluetooth first", Toast.LENGTH_LONG).show()
                }
            }
        }

        scanButton.setOnClickListener {
            requestBluetoothScanPermission {
                startBluetoothDiscovery()
            }
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(receiver, filter)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String? = intent.action
            when (action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    device?.let {
                        if (ActivityCompat.checkSelfPermission(
                                context,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            val deviceName = it.name ?: "Unknown Device"
                            val deviceAddress = it.address
                            val deviceString = "$deviceName - $deviceAddress"
                            if (!devicesList.contains(deviceString)) {
                                devicesList.add(deviceString)
                                devicesAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Toast.makeText(this@MainActivity, "Discovery started", Toast.LENGTH_SHORT).show()
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Toast.makeText(this@MainActivity, "Discovery finished", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateBluetoothStatus() {
        if (BAdpter.isEnabled) {
            bluetoothTv.setImageResource(R.drawable.baseline_bluetooth_24)
        } else {
            bluetoothTv.setImageResource(R.drawable.baseline_bluetooth_disabled_24)
        }
    }

    private fun requestBluetoothConnectPermission(action: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT), REQUEST_CODE_BLUETOOTH_CONNECT)
        } else {
            action()
        }
    }

    private fun requestBluetoothScanPermission(action: () -> Unit) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_SCAN), REQUEST_CODE_BLUETOOTH_SCAN)
        } else {
            action()
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION_PERMISSION)
        }
    }

    private fun startBluetoothDiscovery() {
        requestLocationPermission()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
            checkLocationPermission()) {
            devicesList.clear()
            devicesAdapter.notifyDataSetChanged()
            if (BAdpter.isDiscovering) {
                BAdpter.cancelDiscovery()
            }
            BAdpter.startDiscovery()
        } else {
            Toast.makeText(this, "Permissions required for Bluetooth scanning", Toast.LENGTH_LONG).show()
        }
    }

    private fun makeDeviceDiscoverable() {
        val discoverableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300) // Duration in seconds
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADVERTISE
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
        startActivityForResult(discoverableIntent, REQUEST_CODE_DISCOVERABLE_BT)
    }

    private fun enableBluetooth() {
        if (!BAdpter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
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
            startActivityForResult(enableBtIntent, REQUEST_CODE_ENABLE_BT)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_ENABLE_BT ->
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this@MainActivity, "Bluetooth On", Toast.LENGTH_LONG).show()
                    updateBluetoothStatus()
                } else {
                    Toast.makeText(this@MainActivity, "Could not turn on Bluetooth", Toast.LENGTH_LONG).show()
                }
            REQUEST_CODE_DISCOVERABLE_BT ->
                if (resultCode == Activity.RESULT_CANCELED) {
                    Toast.makeText(this@MainActivity, "Discoverability cancelled", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "Discoverability enabled", Toast.LENGTH_LONG).show()
                }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_BLUETOOTH_CONNECT, REQUEST_CODE_BLUETOOTH_SCAN, REQUEST_CODE_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    when (requestCode) {
                        REQUEST_CODE_BLUETOOTH_CONNECT -> {
                            enableBluetooth()
                        }
                        REQUEST_CODE_BLUETOOTH_SCAN -> {
                            startBluetoothDiscovery()
                        }
                        REQUEST_CODE_LOCATION_PERMISSION -> {
                            startBluetoothDiscovery()
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Permission denied", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
