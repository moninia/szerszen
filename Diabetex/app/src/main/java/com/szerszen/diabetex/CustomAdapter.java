package com.szerszen.diabetex;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Własna klasa Adaptera dla ListView wszystkich produktów
 *
 * Created by Monika Ryczko on 1/8/18.
 * @author Monika Ryczko
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<RowItem> rowItemList;

    /**
     * Konstruktor tworzący własny adapter dla listy produktów
     * @param context
     *          Kontekst
     * @param rowItems
     *          Lista z produktami
     */
    CustomAdapter(Context context, List<RowItem> rowItems) {
        this.context = context;
        this.rowItemList = rowItems;
    }

    /**
     * @return Wielkość listy
     */
    @Override
    public int getCount() {
        return rowItemList.size();
    }

    /**
     *
     * @param position
     *          Pozycja na liście
     * @return Produkt na danej pozycji
     */
    @Override
    public Object getItem(int position) {
        return rowItemList.get(position);
    }

    /**
     * @param position
     *          Pozycja na liście
     * @return Identyfikator produktów dla danej pozycji
     */
    @Override
    public long getItemId(int position) {
        return rowItemList.indexOf(getItemId(position));
    }

    private class ViewHolder {
        ImageView product_pic;
        TextView product_name;
        TextView product_IG;
        TextView product_kcal;
        TextView text_IG;
        TextView text_kcal;
    }

    /**
     * Stworzenie widoku z produktami
     * @param position
     *          Pozycja produktu w adapterze
     * @param convertView
     *          Stary widok to ponownego użycia
     * @param parent
     *          Widok rodzic do którego ten widok będzie przypisany
     * @return Widok pokazujący dane produktów
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        //należy sprawdzić czy podany stary widok jest pusty
        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder();

            holder.product_name = (TextView) convertView.findViewById(R.id.product_name);
            holder.product_IG = (TextView) convertView.findViewById(R.id.product_ig);
            holder.product_kcal = (TextView) convertView.findViewById(R.id.product_kcal);
            holder.product_pic = (ImageView) convertView.findViewById(R.id.product_pic);

            holder.text_IG = (TextView) convertView.findViewById(R.id.textView2);
            holder.text_kcal = (TextView) convertView.findViewById(R.id.text1);

            RowItem row_pos = rowItemList.get(position);

            //Nadać wartości każdemu rzędowi
            holder.product_pic.setImageResource(row_pos.getProduct_pic_id());
            holder.product_name.setText(row_pos.getProduct_name());
            holder.product_IG.setText(row_pos.getProduct_IG());
            holder.product_kcal.setText(row_pos.getProduct_kcal());

            convertView.setTag(holder);
        } else {
            //jeśli stary widok był zapewniony oznacza to, że możemy go ponownie użyć
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setTag(holder);
        return convertView;
    }
}
