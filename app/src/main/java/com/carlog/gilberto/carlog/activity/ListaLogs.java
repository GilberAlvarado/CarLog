package com.carlog.gilberto.carlog.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.TextView;
import com.carlog.gilberto.carlog.R;
import com.carlog.gilberto.carlog.data.dbCar;
import com.carlog.gilberto.carlog.data.dbLogs;
import com.carlog.gilberto.carlog.formats.documentHelper;
import com.carlog.gilberto.carlog.formats.utilities;
import com.carlog.gilberto.carlog.fragments.fragmentHistorial;
import com.carlog.gilberto.carlog.fragments.fragmentLogs;
import com.carlog.gilberto.carlog.negocio.cambiarCocheActivo;
import com.carlog.gilberto.carlog.tiposClases.usuario;
import com.carlog.gilberto.carlog.view.slidingTabLayout;
import com.github.ksoichiro.android.observablescrollview.CacheFragmentStatePagerAdapter;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.TouchInterceptionFrameLayout;
import com.nineoldandroids.view.ViewHelper;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import java.io.File;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Gilberto on 29/10/2014.
 */
public class listaLogs extends baseActivity implements ObservableScrollViewCallbacks  {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private static final int INVALID_POINTER = -1;

    public static DrawerLayout Drawer;
    public static ActionBarDrawerToggle mDrawerToggle;
    private View mImageView;
    private View mImageViewOnClick;
    private View mOverlayView;
    private TextView mTitleView;
    private CircleImageView profileImg;
    private View mFab;
    private int mActionBarSize;
    private int mToolbarColor;
    private View mToolbar;
    private Toolbar toolbar;
    private FloatingActionMenu mFloatMenu;

    private TouchInterceptionFrameLayout mInterceptionLayout;
    private ViewPager mPager;
    private NavigationAdapter mPagerAdapter;
    private VelocityTracker mVelocityTracker;
    private OverScroller mScroller;
    private float mBaseTranslationY;
    private int mMaximumVelocity;
    private int mActivePointerId = INVALID_POINTER;
    private int mSlop;
    private int mFlexibleSpaceHeight;
    private int mTabHeight;
    private boolean mScrolled;

    private void imagenFB(final Activity activity) {
        String idFB = login.getIdFacebook(activity);
        profileImg = (CircleImageView) findViewById(R.id.profilePicture);
        if (idFB != null) {
            try {
                mTitleView = (TextView) findViewById(R.id.title);
                //mTitleView.setText("Próximas revisiones");
                mTitleView.setText(login.getNameFacebook(activity));
                if (login.getImgProfileFacebook(activity) != null) {
                    profileImg.setImageBitmap(login.getImgProfileFacebook(activity));
                }
                else {
                    Thread thread=  new Thread(){
                        @Override
                        public void run(){
                            try {
                                synchronized(this){
                                    while (login.getImgProfileFacebook(activity) == null){
                                        wait(1000);
                                        if (login.getImgProfileFacebook(activity) != null) {
                                            listaLogs.this.runOnUiThread(new Runnable() {
                                                public void run() {
                                                    profileImg.setImageBitmap(login.getImgProfileFacebook(activity));
                                                }
                                            });
                                            break;
                                        }
                                    }
                                }
                            }
                            catch(InterruptedException ex){
                            }
                        }
                    };

                    thread.start();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        /*else { //sesión iniciada por registro
            usuario usuario = new com.carlog.gilberto.carlog.tiposClases.usuario();
            if(usuario.isUserLoggedIn(this)) {
                System.out.println("Logueado");
                usuario.readUser(this);
                String nombre = usuario.getName();
                mTitleView = (TextView) findViewById(R.id.title);
                mTitleView.setText(nombre);
            }
        }*/
        else { //sesión iniciada en modo anónimo
            mTitleView = (TextView) findViewById(R.id.title);
            mTitleView.setText("Anónimo");
        }
    }

    private void ObservableScrollView(Activity activity) {
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        Drawer = (DrawerLayout) activity.findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(activity, Drawer, toolbar, R.string.open_drawer, R.string.close_drawer)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }
        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

        setSupportActionBar(toolbar);
        imagenFB(activity);

        ViewCompat.setElevation(findViewById(R.id.header), getResources().getDimension(R.dimen.toolbar_elevation));
        mPagerAdapter = new NavigationAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mImageView = findViewById(R.id.image);
        mOverlayView = findViewById(R.id.overlay);

        // Padding for ViewPager must be set outside the ViewPager itself
        // because with padding, EdgeEffect of ViewPager become strange.
        mTabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        findViewById(R.id.pager_wrapper).setPadding(0, mFlexibleSpaceHeight + mTabHeight, 0, 0);
        mActionBarSize = getActionBarSize();
        mToolbarColor = getResources().getColor(R.color.ColorPrimary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mToolbar = findViewById(R.id.tool_bar);
        mToolbar.setBackgroundColor(Color.TRANSPARENT);

        mImageView = findViewById(R.id.image);
        mImageViewOnClick = findViewById(R.id.imageonclick);
        mOverlayView = findViewById(R.id.overlay);

        slidingTabLayout slidingTabLayout = (slidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accent));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mPager);
        ((FrameLayout.LayoutParams) slidingTabLayout.getLayoutParams()).topMargin = mFlexibleSpaceHeight;

        ViewConfiguration vc = ViewConfiguration.get(this);
        mSlop = vc.getScaledTouchSlop();
        mMaximumVelocity = vc.getScaledMaximumFlingVelocity();
        mInterceptionLayout = (TouchInterceptionFrameLayout) findViewById(R.id.container);
        mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);
        mScroller = new OverScroller(getApplicationContext());
        ScrollUtils.addOnGlobalLayoutListener(mInterceptionLayout, new Runnable() {
            @Override
            public void run() {
            // Extra space is required to move mInterceptionLayout when it's scrolled.
            // It's better to adjust its height when it's laid out
            // than to adjust the height when scroll events (onMoveMotionEvent) occur
            // because it causes lagging.
            // See #87: https://github.com/ksoichiro/Android-ObservableScrollView/issues/87
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
            lp.height = getScreenHeight() + mFlexibleSpaceHeight - mTabHeight;
            mInterceptionLayout.requestLayout();

            updateFlexibleSpace();
            }
        });

        mTitleView = (TextView) findViewById(R.id.title);
        setTitle(null);
        mFab = findViewById(R.id.fab);

        LinearLayout difuminado = (LinearLayout) findViewById(R.id.difuminado_layout);
        difuminado.setBackgroundColor(ScrollUtils.getColorWithAlpha(0.4f, mToolbarColor));
        buildFloatingMenu();
    }


    private void buildFloatingMenu() {
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setLayoutParams(new FrameLayout.LayoutParams(140, 140));

        // Actualizar coche
        ImageView itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_actualizarcoche));
        itemIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        SubActionButton edytCarButton = itemBuilder.setContentView(itemIcon).build();
        edytCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(listaLogs.this, myActivity.class);
            intent.putExtra("EditarCoche", true);
            startActivityForResult(intent, PETICION_ACTIVITY_DONT_BACK_IF_ADDCAR);
            mFloatMenu.close(true);
            }
        });

        // Añadir nuevo coche
        itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_addcoche));
        itemIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        SubActionButton addCarButton = itemBuilder.setContentView(itemIcon).build();
        addCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(listaLogs.this, myActivity.class);
            intent.putExtra("CocheNuevo", true);
            startActivityForResult(intent, PETICION_ACTIVITY_DONT_BACK_IF_ADDCAR);
            mFloatMenu.close(true);
            // Si agregamos un nuevo coche y volvemos hacia atras se sale de la app pero desde la pantalla de logs puesto que ya hemos agregado un coche y por lo tanto no se queda el drawer sin el coche nuevo al volver atras
            // Si no queremos agregar nuevo coche y pulsamos hacia atras regresamos a la lista de logs anterior
            }
        });

        // Añadir nuevo Log
        itemIcon = new ImageView(this);
        itemIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_addlog));
        itemIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        SubActionButton addLogButton = itemBuilder.setContentView(itemIcon).build();
        addLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(listaLogs.this, addLog.class);
            startActivityForResult(intent, PETICION_ACTIVITY_ADD_LOG);
            mFloatMenu.close(true);
            }
        });

        mFloatMenu = new FloatingActionMenu.Builder(this)
            .addSubActionView(edytCarButton)
            .addSubActionView(addCarButton)
            .addSubActionView(addLogButton)
            .attachTo(mFab)
            .build();
    }



    private void modificarFechasRevisiones(Context context) {
        Boolean modifyItv = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyItv = getIntent().getExtras().getBoolean("modifyItv");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha itv");
        }
        Boolean modifyRevGral = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyRevGral = getIntent().getExtras().getBoolean("modifyRevGral");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha revgral");
        }
        Boolean modifyAceite = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyAceite = getIntent().getExtras().getBoolean("modifyAceite");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha aceite");
        }
        Boolean modifyCorrea = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyCorrea = getIntent().getExtras().getBoolean("modifyCorrea");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha correa");
        }
        Boolean modifyBombaAgua = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyBombaAgua = getIntent().getExtras().getBoolean("modifyBombaAgua");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha bomba de agua");
        }
        Boolean modifyFiltroGasolina = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyFiltroGasolina = getIntent().getExtras().getBoolean("modifyFiltroGasolina");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha filtro de gasolina");
        }
        Boolean modifyFiltroAire = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyFiltroAire = getIntent().getExtras().getBoolean("modifyFiltroAire");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha filtro de aire");
        }
        Boolean modifyBujias = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyBujias = getIntent().getExtras().getBoolean("modifyBujias");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha bujías");
        }
        Boolean modifyEmbrague = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyEmbrague = getIntent().getExtras().getBoolean("modifyEmbrague");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha embrague");
        }
        Boolean modifyPersonalizado = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyPersonalizado = getIntent().getExtras().getBoolean("modifyPersonalizado");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha personalizada");
        }
        Boolean modifyTaller = false;
        try { // Solo si añadimos un coche desde la activity ListaLogs
            modifyTaller = getIntent().getExtras().getBoolean("modifyTaller");
        }
        catch (Exception e) {
            System.out.println("No se ha modificado fecha taller");
        }
        if((modifyItv) || (modifyAceite) || (modifyRevGral) || (modifyCorrea) || (modifyBombaAgua) ||
                (modifyFiltroGasolina) || (modifyFiltroAire) || (modifyBujias) || (modifyEmbrague) || (modifyPersonalizado) || (modifyTaller)) {
            fragmentLogs fl = (fragmentLogs) getCurrentFragment();
            fl.ConsultarLogs(context, listaLogs.this); // para actualizar la fecha de revision modificada
        }
    }

    public void changeImgDrawerObservable() {
        View iv = (View) findViewById(R.id.imageonclick);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                utilities.selectImage(listaLogs.this);
            }
        });
    }


    private class NavigationAdapter extends CacheFragmentStatePagerAdapter {

        private final String[] TITLES = new String[]{listaLogs.this.getString(R.string.revisiones), listaLogs.this.getString(R.string.historial)};

        public NavigationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        protected Fragment createItem(int position) {
            Fragment f;
            final int pattern = position % 3;
            switch (pattern) {
                case 0:
                    f = new fragmentLogs();
                    break;
                case 1:
                    f = new fragmentHistorial();
                    break;
                case 4:
                default:
                    f = new fragmentLogs();
                    break;
            }
            return f;
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listalogs);
        Context context = this;

        ObservableScrollView(listaLogs.this);
        modificarFechasRevisiones(context);
        dbCar dbcar = new dbCar(context);
        Cursor c = dbcar.buscarCoches();
        cambiarCocheActivo.CambiarCocheActivo(dbcar, c, listaLogs.this, context);
        c = dbcar.buscarCocheActivo();
        changeImgDrawerObservable();

        int img_modelo_cambiada = dbCar.IMG_MODELO_NOCAMBIADA;
        String img_modelo_personalizada = "";
        String modelo = "";
        String matricula = "";
        if (c.moveToFirst() == true) {
            matricula = c.getString(c.getColumnIndex(dbCar.CN_MATRICULA));
            modelo = c.getString(c.getColumnIndex(dbCar.CN_MODELO));
            img_modelo_cambiada = c.getInt(c.getColumnIndex(dbCar.CN_IMG_MODELO_CAMBIADA));
            img_modelo_personalizada = c.getString(c.getColumnIndex(dbCar.CN_IMG_MODELO_PERSONALIZADA));
        }
        cambiarCocheActivo.CambiarImgLogs(context, listaLogs.this, matricula, img_modelo_personalizada, modelo, img_modelo_cambiada);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
     /*   // Translate FAB
        int maxFabTranslationY = mFlexibleSpaceHeight - mFab.getHeight() / 3;
        float fabTranslationY = ScrollUtils.getFloat(
                -scrollY + mFlexibleSpaceHeight - mFab.getHeight() / 3,
                mActionBarSize - mFab.getHeight() / 3,
                maxFabTranslationY);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
            // which causes FAB's OnClickListener not working.
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
            lp.leftMargin = mOverlayView.getWidth() - mFabMargin - mFab.getWidth();
            lp.topMargin = (int) fabTranslationY;
            mFab.requestLayout();
        } else {
            ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
            ViewHelper.setTranslationY(mFab, fabTranslationY);
        }

        // Show/hide FAB
        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
            hideFab();
        } else {
            showFab();
        }
       /* if (TOOLBAR_IS_STICKY) {
            // Change alpha of toolbar background
            if (-scrollY + mFlexibleSpaceHeight <= mActionBarSize) {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(1, mToolbarColor));
            } else {
                mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, mToolbarColor));
            }
        } else {
            // Translate Toolbar
            if (scrollY < mFlexibleSpaceHeight) {
                ViewHelper.setTranslationY(mToolbar, 0);
            } else {
                ViewHelper.setTranslationY(mToolbar, -scrollY);
            }
        }*/

    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }


    private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
        @Override
        public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
            if (!mScrolled && mSlop < Math.abs(diffX) && Math.abs(diffY) < Math.abs(diffX)) {
                // Horizontal scroll is maybe handled by ViewPager
                return false;
            }

            Scrollable scrollable = getCurrentScrollable();
            if (scrollable == null) {
                mScrolled = false;
                return false;
            }

            // If interceptionLayout can move, it should intercept.
            // And once it begins to move, horizontal scroll shouldn't work any longer.
            int flexibleSpace = mFlexibleSpaceHeight - mTabHeight;
            int translationY = (int) ViewHelper.getTranslationY(mInterceptionLayout);
            boolean scrollingUp = 0 < diffY;
            boolean scrollingDown = diffY < 0;
            if (scrollingUp) {
                if (translationY < 0) {
                    mScrolled = true;
                    return true;
                }
            } else if (scrollingDown) {
                if (-flexibleSpace < translationY) {
                    mScrolled = true;
                    return true;
                }
            }
            mScrolled = false;
            return false;
        }

        @Override
        public void onDownMotionEvent(MotionEvent ev) {
            mActivePointerId = ev.getPointerId(0);
            mScroller.forceFinished(true);
            if (mVelocityTracker == null) {
                mVelocityTracker = VelocityTracker.obtain();
            } else {
                mVelocityTracker.clear();
            }
            mBaseTranslationY = ViewHelper.getTranslationY(mInterceptionLayout);
            mVelocityTracker.addMovement(ev);
        }

        @Override
        public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
            int flexibleSpace = mFlexibleSpaceHeight - mTabHeight;
            float translationY = ScrollUtils.getFloat(ViewHelper.getTranslationY(mInterceptionLayout) + diffY, -flexibleSpace, 0);
            MotionEvent e = MotionEvent.obtainNoHistory(ev);
            e.offsetLocation(0, translationY - mBaseTranslationY);
            mVelocityTracker.addMovement(e);
            updateFlexibleSpace(translationY);
        }

        @Override
        public void onUpOrCancelMotionEvent(MotionEvent ev) {
            mScrolled = false;
            mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
            int velocityY = (int) mVelocityTracker.getYVelocity(mActivePointerId);
            mActivePointerId = INVALID_POINTER;
            mScroller.forceFinished(true);
            int baseTranslationY = (int) ViewHelper.getTranslationY(mInterceptionLayout);

            int minY = -(mFlexibleSpaceHeight - mTabHeight);
            int maxY = 0;
            mScroller.fling(0, baseTranslationY, 0, velocityY, 0, 0, minY, maxY);
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    updateLayout();
                }
            });
        }
    };


    private void updateLayout() {
        boolean needsUpdate = false;
        float translationY = 0;
        if (mScroller.computeScrollOffset()) {
            translationY = mScroller.getCurrY();
            int flexibleSpace = mFlexibleSpaceHeight - mTabHeight;
            if (-flexibleSpace <= translationY && translationY <= 0) {
                needsUpdate = true;
            } else if (translationY < -flexibleSpace) {
                translationY = -flexibleSpace;
                needsUpdate = true;
            } else if (0 < translationY) {
                translationY = 0;
                needsUpdate = true;
            }
        }

        if (needsUpdate) {
            updateFlexibleSpace(translationY);

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    updateLayout();
                }
            });
        }
    }

    private Scrollable getCurrentScrollable() {
        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return null;
        }
        View view = fragment.getView();
        if (view == null) {
            return null;
        }
        return (Scrollable) view.findViewById(R.id.list);
    }

    private void updateFlexibleSpace() {
        updateFlexibleSpace(ViewHelper.getTranslationY(mInterceptionLayout));
    }

    private void updateFlexibleSpace(float translationY) {
        ViewHelper.setTranslationY(mInterceptionLayout, translationY);
        int minOverlayTransitionY = getActionBarSize() - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-translationY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        float flexibleRange = mFlexibleSpaceHeight - getActionBarSize();
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat(-translationY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange + translationY - mTabHeight) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        setPivotXToTitle();
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);
        // Scale image fb
        ViewHelper.setPivotX(profileImg, 0);
        ViewHelper.setPivotY(profileImg, 0);
        ViewHelper.setScaleX(profileImg, scale);
        ViewHelper.setScaleY(profileImg, scale);

        // Translate title text
        int maxTitleTranslationY = (int) (mFlexibleSpaceHeight - mTitleView.getHeight()  * scale);
        int titleTranslationY = maxTitleTranslationY + (int) translationY - 10; // el -10 es solo para ajustar bien tambien en horizontal

        ViewHelper.setTranslationY(mTitleView, titleTranslationY);
        ViewHelper.setTranslationX(mTitleView, 90);

        // Translate fb image
        ViewHelper.setTranslationY(profileImg, titleTranslationY);
        ViewHelper.setTranslationX(profileImg, 90);
    }

    public Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setPivotXToTitle() {
        Configuration config = getResources().getConfiguration();
        if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            ViewHelper.setPivotX(mTitleView, findViewById(android.R.id.content).getWidth());
        } else {
            ViewHelper.setPivotX(mTitleView, 0);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(listaLogs.this, settings.class);
            listaLogs.this.startActivity(i);
        }
        if (id == R.id.action_info) {
            Intent i = new Intent(listaLogs.this, info.class);
            listaLogs.this.startActivity(i);
        }
        if (id == R.id.action_logout) {
            if (login.getIdFacebook(this) == null){
                login.goToLoginScreen(this);
                usuario u = new usuario();
                u.logout(listaLogs.this);
                login.deleteParamsAnonimo(listaLogs.this);
                Intent intent = new Intent(listaLogs.this, login.class);
                startActivity(intent);
            } else {
                usuario u = new usuario();
                u.logout(listaLogs.this);
                login.deleteParamsAnonimo(listaLogs.this);
                login.closeFacebookSession(this, login.class);
                Intent intent = new Intent(listaLogs.this, login.class);
                startActivity(intent);
            }
            finish();
        }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        try {
            fragmentLogs fl = (fragmentLogs) getCurrentFragment();
            menu.add(0, 0, 0, listaLogs.this.getString(R.string.realizadohoy));
            menu.add(1, 1, 1, listaLogs.this.getString(R.string.realizadofecha));
            menu.add(2, 2, 2, listaLogs.this.getString(R.string.eliminar));
        }
        catch (Exception e) {
            try {
                fragmentHistorial fh = (fragmentHistorial) getCurrentFragment();
                menu.add(3, 3, 3, listaLogs.this.getString(R.string.eliminar));
            }
            catch (Exception e2) {

            }
        }

     //   MenuInflater inflater = getMenuInflater();
//        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
 //       inflater.inflate(R.menu.modificar_log, menu);
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final dbLogs manager = new dbLogs(this);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        dbCar dbc = new dbCar(this);
        Cursor c_activo = dbc.buscarCocheActivo();
        String matricula = "";
        if (c_activo.moveToFirst() == true) {
            matricula = c_activo.getString(c_activo.getColumnIndex(dbCar.CN_MATRICULA));
        }

        final Cursor cursor = manager.LogsFuturosOrderByFechaString(matricula);
        final Cursor cursor_hist = manager.LogsHistoricosOrderByFechaString(matricula);

        switch (item.getItemId()) {
            //case R.id.menu_realizadoLog:
            case 0:
                fragmentLogs.realizadoLogpulsado(cursor, manager, info.position, true, getApplicationContext(), listaLogs.this);
                return true;
            case 1:
                fragmentLogs.realizadoLogpulsado(cursor, manager, info.position, false, getApplicationContext(), listaLogs.this);
                return true;
            //case R.id.menu_eliminarLog:
            case 2:
                fragmentLogs.borrarLogpulsado(cursor, manager, info.position, getApplicationContext(), listaLogs.this);
                return true;
            case 3:
                fragmentHistorial.borrarLogpulsado(cursor_hist, manager, info.position, getApplicationContext(), listaLogs.this);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    public static final int PETICION_ACTIVITY_ADD_LOG = 1;
    public static final int PETICION_ACTIVITY_DONT_BACK_IF_ADDCAR = 2;
    public static final int PETICION_ACTIVITY_MODIFYITV = 3;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("result "+ requestCode);
        switch(requestCode) {
            case (PETICION_ACTIVITY_ADD_LOG) : {
                if (resultCode == Activity.RESULT_OK) {
                    fragmentLogs fl = (fragmentLogs) getCurrentFragment();
                    fl.ConsultarLogs(this, listaLogs.this);

                }
                break;
            }
            // Si agregamos un nuevo coche y volvemos hacia atras se sale de la app pero desde la pantalla de logs puesto que ya hemos agregado un coche y por lo tanto no se queda el drawer sin el coche nuevo al volver atras
            // Si no queremos agregar nuevo coche y pulsamos hacia atras regresamos a la lista de logs anterior
            case (PETICION_ACTIVITY_DONT_BACK_IF_ADDCAR) : {
                if(resultCode == Activity.RESULT_OK) {
                    finish();
                }
                break;
            }
            case (PETICION_ACTIVITY_MODIFYITV) : {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        fragmentLogs fl = (fragmentLogs) getCurrentFragment();
                        fl.ConsultarLogs(getApplicationContext(), listaLogs.this);
                    } catch (Exception e) {
                        fragmentHistorial fh = (fragmentHistorial) getCurrentFragment();
                        fh.ConsultarLogsHistoricos(getApplicationContext(),listaLogs.this);
                    }
                }
                break;
            }
            case (utilities.GALLERY_INTENT) : {
                if (data == null)
                    return;
                Uri selectedPictureUri = data.getData();
                if (selectedPictureUri == null)
                    return;
                try {
                    Bitmap img_fondo = utilities.getBitMapFromUri(this, selectedPictureUri);
                    File chosenFile = new File(documentHelper.getPath(this, selectedPictureUri));

                    dbCar dbc = new dbCar(getApplicationContext());
                    Cursor c_act = dbc.buscarCocheActivo();
                    if (c_act.moveToFirst() == true) {
                        String matricula = c_act.getString(c_act.getColumnIndex(dbCar.CN_MATRICULA));
                        String uriEncoded = Uri.encode(documentHelper.getPath(this, selectedPictureUri), "UTF-8");
                        dbc.ActualizarImgModelo(matricula, uriEncoded);
                        ImageView img_listalogs = (ImageView) listaLogs.this.findViewById(R.id.image);
                        img_listalogs.setImageURI(selectedPictureUri);
                        cambiarCocheActivo.ActualizarCochesDrawer(dbc, listaLogs.this, getApplicationContext());
                    }
                    //File chosenFile = new File(DocumentHelper.getPath(this, selectedPictureUri));
                   // Utilities.changeImage(this, chosenFile, Utilities.getBitMapFromUri(this, selectedPictureUri), selectedPictureUri.getPath(), LoginMethods.getIdFacebook(this), mSendero.getServerId(), 0, 0, pd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case (utilities.CAMERA_INTENT) : {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals(listaLogs.this.getString(R.string.temp_picture))) {
                        f = temp;
                        break;
                    }
                }
                Uri.fromFile(f);
                try {
                    dbCar dbc = new dbCar(getApplicationContext());
                    Cursor c_act = dbc.buscarCocheActivo();
                    if (c_act.moveToFirst() == true) {
                        String matricula = c_act.getString(c_act.getColumnIndex(dbCar.CN_MATRICULA));
                        String uriEncoded = Uri.encode(documentHelper.getPath(this, Uri.fromFile(f)), "UTF-8");
                        dbc.ActualizarImgModelo(matricula, uriEncoded);
                        ImageView img_listalogs = (ImageView) listaLogs.this.findViewById(R.id.image);
                        img_listalogs.setImageURI(Uri.fromFile(f));
                        cambiarCocheActivo.ActualizarCochesDrawer(dbc, listaLogs.this, getApplicationContext());
                    }
                    //File chosenFile = new File(DocumentHelper.getPath(this, selectedPictureUri));
                    // Utilities.changeImage(this, chosenFile, Utilities.getBitMapFromUri(this, selectedPictureUri), selectedPictureUri.getPath(), LoginMethods.getIdFacebook(this), mSendero.getServerId(), 0, 0, pd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mFloatMenu.isOpen()) {
                mFloatMenu.close(true);
                return true;
            }
            else {
                return super.onKeyDown(keyCode, event);
            }
        }
        //para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }

}
