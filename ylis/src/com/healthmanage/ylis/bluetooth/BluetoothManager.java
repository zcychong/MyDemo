package com.healthmanage.ylis.bluetooth;

import android.bluetooth.BluetoothAdapter;

public class BluetoothManager {
	private BluetoothAdapter bluetoothAdapter;
	
	public BluetoothManager(){
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	}
	/**
	 * 是否支持蓝牙
	 * @return
	 */
    public static boolean isBluetoothSupported()
    {
    	
        return BluetoothAdapter.getDefaultAdapter() != null ? true : false;
    }
 
    public BluetoothAdapter getBluetoothAdapter() {
		return bluetoothAdapter;
	}
	public void setBluetoothAdapter(BluetoothAdapter bluetoothAdapter) {
		this.bluetoothAdapter = bluetoothAdapter;
	}
	/**
	 * 蓝牙是否开启
	 * @return
	 */
    public static boolean isBluetoothEnabled()
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
 
        if (bluetoothAdapter != null)
        {
            return bluetoothAdapter.isEnabled();
        }
 
        return false;
    }
 
    /**
     * 开启蓝牙
     * @return
     */
    public static boolean turnOnBluetooth()
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
 
        if (bluetoothAdapter != null)
        {
            return bluetoothAdapter.enable();
        }
 
        return false;
    }
    
    /**
     * 关闭蓝牙
     * @return
     */
    public static boolean turnOffBluetooth()
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
 
        if (bluetoothAdapter != null)
        {
            return bluetoothAdapter.disable();
        }
 
        return false;
    }


}
