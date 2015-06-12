package com.carlog.gilberto.carlog.adapter;

        import android.content.Context;
        import android.database.Cursor;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import com.balysv.materialripple.MaterialRippleLayout;
        import com.carlog.gilberto.carlog.R;
        import com.carlog.gilberto.carlog.tiposClases.TipoCoche;
        import com.carlog.gilberto.carlog.data.DBCar;
        import com.carlog.gilberto.carlog.data.DBMarcas;
        import com.carlog.gilberto.carlog.data.DBModelos;

        import java.util.ArrayList;
        import java.util.List;


/**
 * Created by Gilberto on 31/05/2015.
 */



public class miAdaptadorCoches extends RecyclerView.Adapter<miAdaptadorCoches.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private View.OnClickListener listener;
    private View.OnLongClickListener longlistener;

    private static final int TYPE_HEADER = 0;  // Declaring Variable to Understand which View is being worked on
    // IF the view under inflation and population is header or Item
    private static final int TYPE_ITEM = 1;

    private String mNavMatriculas[];
    private Integer[] mIcons;       // iconos q se muestran en el drawer

    private String marca;        //String Resource for header View Name
    private int img_marca;
    private int img_modelo;
    private String modelo;       //String Resource for header view email




    // Creating a ViewHolder which extends the RecyclerView View Holder
    // ViewHolder are used to to store the inflated views in order to recycle them

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView img_marca;
        ImageView img_modelo;
        TextView Marca;
        TextView modelo;


        public ViewHolder(View itemView,int ViewType) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);


            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if(ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;                                               // setting holder id as 1 as the object being populated are of type item row
            }
            else{


                Marca = (TextView) itemView.findViewById(R.id.marca);         // Creating Text View object from header.xml for name
                modelo = (TextView) itemView.findViewById(R.id.modelo);       // Creating Text View object from header.xml for email
                img_marca = (ImageView) itemView.findViewById(R.id.circleView);// Creating Image view object from header.xml for profile pic
                img_modelo = (ImageView) itemView.findViewById(R.id.background_modelo);
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }



    }



    public miAdaptadorCoches(Cursor c, Context context){ // MyAdapter Constructor with titles and icons parameter



        List<String> lista_matriculas = new ArrayList<String>();
        List<Integer> lista_iconos = new ArrayList<Integer>();
        //Recorremos el cursor
        int i = 0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String matricula = c.getString(c.getColumnIndex(DBCar.CN_MATRICULA));
            int profile = c.getInt(c.getColumnIndex(DBCar.CN_PROFILE));
            if(profile == TipoCoche.PROFILE_ACTIVO) {
                marca = c.getString(c.getColumnIndex(DBCar.CN_MARCA));
                modelo = c.getString(c.getColumnIndex(DBCar.CN_MODELO));
            }
            /*
            int_year = c.getInt(c.getColumnIndex(DBCar.CN_YEAR));
            int_kms = c.getInt(c.getColumnIndex(DBCar.CN_KMS));
            int_itv = c.getInt(c.getColumnIndex(DBCar.CN_ITV));
            int_kms_ini = c.getInt(c.getColumnIndex(DBCar.CN_KMS_INI));
            int_fecha_ini = c.getInt(c.getColumnIndex(DBCar.CN_FECHA_INI));
            year = String.valueOf(int_year);
            kms = String.valueOf(int_kms);
            itv = funciones.int_a_string(int_itv);
            int_kms_anterior = int_kms;*/

            lista_matriculas.add(matricula);
            lista_iconos.add(R.drawable.ic_coche);

        }


        String[] array_matriculas = new String[lista_matriculas.size()];
        for (int j = 0; j < lista_matriculas.size(); j++) {
            array_matriculas[j] = lista_matriculas.get(j);
        }
        Integer[] array_iconos = new Integer[lista_iconos.size()];
        for (int j = 0; j < lista_iconos.size(); j++) {
            array_iconos[j] = lista_iconos.get(j);
        }

        if (c.moveToFirst() == true) {
            DBMarcas dbmarcas = new DBMarcas(context);
            Cursor c_marca = dbmarcas.buscarMarcas(marca);
            if (c_marca.moveToFirst() == true) {

                String mDrawableImg = c_marca.getString(c_marca.getColumnIndex(DBMarcas.CN_IMG));
                int resID = context.getResources().getIdentifier(mDrawableImg, "drawable", context.getPackageName());
                img_marca = resID;
            }
            DBModelos dbmodelos = new DBModelos(context);
            Cursor c_modelo = dbmodelos.buscarModelos(modelo);

            if (c_modelo.moveToFirst() == true) {

                String mDrawableImg = c_modelo.getString(c_modelo.getColumnIndex(DBModelos.CN_IMG));
                int resID = context.getResources().getIdentifier(mDrawableImg, "drawable", context.getPackageName());
                img_modelo = resID;
            }


        }
        else {
            img_modelo = R.drawable.modelo_inicio;
            img_marca = R.drawable.logo_inicio;
        }

        mNavMatriculas = array_matriculas;
        mIcons = array_iconos;

        //in adapter



    }




    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder



    @Override
    public miAdaptadorCoches.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false); //Inflating the layout

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);

            ViewHolder vhItem = new ViewHolder(
                    MaterialRippleLayout.on(v)
                            .rippleOverlay(true)
                            .rippleAlpha(0.2f)
                            .rippleColor(0xFF585858)
                            .rippleHover(true)
                            .create()
                    ,viewType);


            //ViewHolder vhItem = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header,parent,false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v,viewType); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;

    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setOnLongClickListener(View.OnLongClickListener longlistener) {
        this.longlistener = longlistener;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onClick(view);
    }

    @Override
    public boolean onLongClick(View view) {
        if(longlistener != null) {
            longlistener.onLongClick(view);
            return true;
        }
        else return false;
    }

    public String getMatriculaSeleccionada(int posicion) {
        return mNavMatriculas[posicion];
    }

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(miAdaptadorCoches.ViewHolder holder, int position) {
        if(holder.Holderid == 1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavMatriculas[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(mIcons[position -1]);// Settimg the image with array of our icons

        }
        else{
            holder.img_modelo.setBackgroundResource(img_modelo);
            holder.img_marca.setImageResource(img_marca);           // Similarly we set the resources for header view
            holder.Marca.setText(marca);
            holder.modelo.setText(modelo);
        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavMatriculas.length+1; // the number of items in the list will be +1 the titles including the header view.
    }


    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

}