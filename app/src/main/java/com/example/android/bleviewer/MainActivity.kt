package com.example.android.bleviewer

import android.Manifest
import android.os.Bundle
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : FragmentActivity() {

    lateinit var m_bluetoothAdapter : BluetoothAdapter
    lateinit var m_spinnerAdapter : ArrayAdapter<String>
    lateinit var popupMsg : PopupMessage

    var m_deviceList = mutableListOf<String>()
    var m_scannerRunning = false
    val m_scanCallback = Callback()


    fun updateTextField(text : String) {
        //val v = supportFragmentManager.findFragmentByTag("details")

        for(fragment in supportFragmentManager.fragments) {
            if (fragment is TextFragment) {
                fragment.updateText(text)
            }
        }
    }

    inner class Callback : ScanCallback()
    {
        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)

            popupMsg.show("Scan Error: ${0}".format(errorCode.toString()))
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            if(result == null || result.device == null)
                return

            if(result.device.name != null) {
                if (result.device.name !in m_deviceList) {
                    m_deviceList.add(result.device.name)
                    m_spinnerAdapter.notifyDataSetChanged()
                }
            }

            if(spinner_devices.selectedItem != null) {
                if (result.device.name == spinner_devices.selectedItem.toString()) {
                    updateTextField(result.scanRecord?.toString() ?: "No record")
                    //editText_adv.append("\n {0}".format(result.scanRecord?.manufacturerSpecificData?.get(0)))
                }
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
        }
    }


    inner class PagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {

            return when(position) {
                0 -> ChartFragment.newInstance()
                else -> TextFragment.newInstance("TEST 2")
            }
        }

        override fun getCount(): Int {
            return 2
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pager.adapter = PagerAdapter(getSupportFragmentManager())

        popupMsg = PopupMessage(this)

        m_spinnerAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, m_deviceList)
        m_spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_devices.adapter = m_spinnerAdapter

        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }

        var bluetoothManager : BluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        m_bluetoothAdapter = bluetoothManager.adapter

        if(!m_bluetoothAdapter.isEnabled)
            m_bluetoothAdapter.enable()


        button_scan.setOnClickListener()
        {
            if(!m_scannerRunning)
                m_bluetoothAdapter.bluetoothLeScanner.startScan(m_scanCallback)
            else
                m_bluetoothAdapter.bluetoothLeScanner.stopScan(m_scanCallback)

            m_scannerRunning = !m_scannerRunning

            if(m_scannerRunning)
                button_scan.text = "Stop"
            else
                button_scan.text = "Scan"
        }
    }
}
