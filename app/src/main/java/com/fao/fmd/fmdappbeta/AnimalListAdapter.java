package com.fao.fmd.fmdappbeta;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AnimalListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;

    public AnimalListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.animal_item, parent,false);
        }

        TextView txtListChild = convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);

        ImageButton edit = convertView.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String name = childText.substring(childText.indexOf(' ')+1, childText.indexOf('\n'));
                String selectID = "SELECT * FROM " + Animal.AnimalEntry.TABLE_NAME + " WHERE name='" + name + "'";
                DatabaseHelper mDbHelper = new DatabaseHelper(_context);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                Cursor cursorID = db.rawQuery(selectID, null);
                cursorID.moveToFirst();
                int id = cursorID.getInt(cursorID.getColumnIndex(Animal.AnimalEntry.COLUMN_ID));
                cursorID.close();
                Bundle b = new Bundle();
                b.putInt("id", id);
                Intent intent = new Intent(_context, EditAnimal.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.putExtras(b);
                _context.startActivity(intent);
            }
        });

        Button del = convertView.findViewById(R.id.del);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                String name = childText.substring(childText.indexOf(' ')+1, childText.indexOf('\n'));
                                DatabaseHelper mDbHelper = new DatabaseHelper(_context);
                                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                                db.delete(Animal.AnimalEntry.TABLE_NAME,Animal.AnimalEntry.COLUMN_NAME + "='" + name + "'",null);
                                Toast.makeText(_context, "Animal deleted", Toast.LENGTH_SHORT).show();
                                _listDataHeader.remove(childPosition);
                                notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                builder.setMessage("Are you sure to delete this animal?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_animal, parent, false);
        }

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
