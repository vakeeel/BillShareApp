package com.vins.tab;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ViewBillsTabAdapter extends BaseAdapter{

	private Context context;
	private List<BillInfo> billInfo;
	
	public ViewBillsTabAdapter(Context _context, List<BillInfo> _billInfo) {
		this.context = _context;
		this.billInfo = _billInfo;
	}

	@Override
	public int getCount() {
		return billInfo.size();
	}

	@Override
	public Object getItem(int position) {
		return billInfo.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
public View getView(int position, View convertView, ViewGroup parent) {
		
		BillInfo info = billInfo.get(position);
		
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.viewbills_lv, null);
        }
        
        TextView billDescription = (TextView) convertView.findViewById(R.id.viewBillsLVBillDescriptionTVID);
        billDescription.setText(info.getDescription());

        TextView billAmount = (TextView) convertView.findViewById(R.id.viewBillsLVAmountTVID);
        billAmount.setText(info.getBillAmount());
        
        return convertView;
	}

}
