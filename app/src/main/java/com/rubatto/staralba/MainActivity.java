package com.rubatto.staralba;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;
    private TextView mAdTv;
    private TabLayout mTabLayout;
    private LinearLayout contaioner;
    private FloatingActionButton mfloatingBt;

    private ImageView mNavProfileImg;
    private TextView mNavNicknameTv;

    private String mSharedNickname;

    private boolean isNear = true;
    private boolean isList = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAdTv = (TextView) findViewById(R.id.main_ad_tv);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        contaioner = (LinearLayout) findViewById(R.id.main_fragment_container);
        mfloatingBt = (FloatingActionButton) findViewById(R.id.main_floating_bt);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.main_tab_label1));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.main_tab_label2));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.main_tab_label3));
        mTabLayout.setTabTextColors(getResources().getColor(android.R.color.background_dark), getResources().getColor(R.color.colorAccent));

        // 근처 업체 리사이클러 표시
        replaceFragment(new MainSurroundFragment());

        mAdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ManageAdActivity.class);
                startActivity(i);
            }
        });

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(MainActivity.this, tab.getPosition() + "", Toast.LENGTH_SHORT).show();
                if (tab.getPosition() == 0) {
                    isNear = true;
                    isList = true;
                    mfloatingBt.setImageDrawable(getResources().getDrawable(R.drawable.floating_pin));
                    replaceFragment(new MainSurroundFragment());
                } else if (tab.getPosition() == 1) {
                    isNear = false;
                    mfloatingBt.setImageDrawable(getResources().getDrawable(R.drawable.floating_search));
                    // TODO: 2017-09-20 카테고리별 출력
                    replaceFragment(new MainCategoryFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Toast.makeText(MainActivity.this, "onTabUnselected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Toast.makeText(MainActivity.this, "onTabReselected", Toast.LENGTH_SHORT).show();
            }
        });

        mfloatingBt = (FloatingActionButton) findViewById(R.id.main_floating_bt);
        mfloatingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNear) {
                    if (isList) {
                        mfloatingBt.setImageDrawable(getResources().getDrawable(R.drawable.floating_menu));
                        replaceFragment(new MainWebviewFragment());
                        isList = false;
                    } else {
                        mfloatingBt.setImageDrawable(getResources().getDrawable(R.drawable.floating_pin));
                        replaceFragment(new MainSurroundFragment());
                        isList = true;
                    }
                } else {
                    //mfloatingBt.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_search));
                    //// TODO: 2017-09-20 dialog 띄움
                    LayoutInflater inflater = getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.main_category_area_dialog, null);
                    AlertDialog.Builder buider = new AlertDialog.Builder(MainActivity.this); //AlertDialog.Builder 객체 생성
                    buider.setTitle("관심지역 선택"); //Dialog 제목
                    buider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)

                    final AlertDialog dialog = buider.create();
                    dialog.setCanceledOnTouchOutside(false);//없어지지 않도록 설정
                    dialog.show();

                    ArrayList<String> areaList = new ArrayList<String>();
                    areaList.add("");
                    areaList.add("서울특별시");
                    areaList.add("부산광역시");
                    areaList.add("대구광역시");
                    areaList.add("인천광역시");
                    areaList.add("광주광역시");
                    areaList.add("대전광역시");
                    areaList.add("울산광역시");
                    areaList.add("강원도");
                    areaList.add("경기도");
                    areaList.add("경상북도");
                    areaList.add("경상남도");
                    areaList.add("전라북도");
                    areaList.add("전라남도");
                    areaList.add("충청북도");
                    areaList.add("충청남도");
                    areaList.add("제주특별자치도");

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, areaList);

                    final Spinner spinner1 = (Spinner) dialogView.findViewById(R.id.main_category_area_dialog_spinner1);
                    final Spinner spinner2 = (Spinner) dialogView.findViewById(R.id.main_category_area_dialog_spinner2);
                    final Spinner spinner3 = (Spinner) dialogView.findViewById(R.id.main_category_area_dialog_spinner3);

                    spinner1.setAdapter(arrayAdapter);
                    spinner2.setAdapter(arrayAdapter);
                    spinner3.setAdapter(arrayAdapter);

                    if (DataArea.getFirstArea().isEmpty() || DataArea.getFirstArea().equals("")) {
                        spinner1.setSelection(0);
                    } else if (DataArea.getFirstArea().equals("서울특별시")) {
                        spinner1.setSelection(1);
                    } else if (DataArea.getFirstArea().equals("부산광역시")) {
                        spinner1.setSelection(2);
                    } else if (DataArea.getFirstArea().equals("대구광역시")) {
                        spinner1.setSelection(3);
                    } else if (DataArea.getFirstArea().equals("인천광역시")) {
                        spinner1.setSelection(4);
                    } else if (DataArea.getFirstArea().equals("광주광역시")) {
                        spinner1.setSelection(5);
                    } else if (DataArea.getFirstArea().equals("대전광역시")) {
                        spinner1.setSelection(6);
                    } else if (DataArea.getFirstArea().equals("울산광역시")) {
                        spinner1.setSelection(7);
                    } else if (DataArea.getFirstArea().equals("강원도")) {
                        spinner1.setSelection(8);
                    } else if (DataArea.getFirstArea().equals("경기도")) {
                        spinner1.setSelection(9);
                    } else if (DataArea.getFirstArea().equals("경상북도")) {
                        spinner1.setSelection(10);
                    } else if (DataArea.getFirstArea().equals("경상남도")) {
                        spinner1.setSelection(11);
                    } else if (DataArea.getFirstArea().equals("전라북도")) {
                        spinner1.setSelection(12);
                    } else if (DataArea.getFirstArea().equals("전라남도")) {
                        spinner1.setSelection(13);
                    } else if (DataArea.getFirstArea().equals("충청북도")) {
                        spinner1.setSelection(14);
                    } else if (DataArea.getFirstArea().equals("충청남도")) {
                        spinner1.setSelection(15);
                    } else if (DataArea.getFirstArea().equals("제주특별자치도")) {
                        spinner1.setSelection(16);
                    }

                    if (DataArea.getSecondArea().isEmpty() || DataArea.getSecondArea().equals("")) {
                        spinner2.setSelection(0);
                    } else if (DataArea.getSecondArea().equals("서울특별시")) {
                        spinner2.setSelection(1);
                    } else if (DataArea.getSecondArea().equals("부산광역시")) {
                        spinner2.setSelection(2);
                    } else if (DataArea.getSecondArea().equals("대구광역시")) {
                        spinner2.setSelection(3);
                    } else if (DataArea.getSecondArea().equals("인천광역시")) {
                        spinner2.setSelection(4);
                    } else if (DataArea.getSecondArea().equals("광주광역시")) {
                        spinner2.setSelection(5);
                    } else if (DataArea.getSecondArea().equals("대전광역시")) {
                        spinner2.setSelection(6);
                    } else if (DataArea.getSecondArea().equals("울산광역시")) {
                        spinner2.setSelection(7);
                    } else if (DataArea.getSecondArea().equals("강원도")) {
                        spinner2.setSelection(8);
                    } else if (DataArea.getSecondArea().equals("경기도")) {
                        spinner2.setSelection(9);
                    } else if (DataArea.getSecondArea().equals("경상북도")) {
                        spinner2.setSelection(10);
                    } else if (DataArea.getSecondArea().equals("경상남도")) {
                        spinner2.setSelection(11);
                    } else if (DataArea.getSecondArea().equals("전라북도")) {
                        spinner2.setSelection(12);
                    } else if (DataArea.getSecondArea().equals("전라남도")) {
                        spinner2.setSelection(13);
                    } else if (DataArea.getSecondArea().equals("충청북도")) {
                        spinner2.setSelection(14);
                    } else if (DataArea.getSecondArea().equals("충청남도")) {
                        spinner2.setSelection(15);
                    } else if (DataArea.getSecondArea().equals("제주특별자치도")) {
                        spinner2.setSelection(16);
                    }

                    if (DataArea.getThirdArea().isEmpty() || DataArea.getThirdArea().equals("")) {
                        spinner3.setSelection(0);
                    } else if (DataArea.getThirdArea().equals("서울특별시")) {
                        spinner3.setSelection(1);
                    } else if (DataArea.getThirdArea().equals("부산광역시")) {
                        spinner3.setSelection(2);
                    } else if (DataArea.getThirdArea().equals("대구광역시")) {
                        spinner3.setSelection(3);
                    } else if (DataArea.getThirdArea().equals("인천광역시")) {
                        spinner3.setSelection(4);
                    } else if (DataArea.getThirdArea().equals("광주광역시")) {
                        spinner3.setSelection(5);
                    } else if (DataArea.getThirdArea().equals("대전광역시")) {
                        spinner3.setSelection(6);
                    } else if (DataArea.getThirdArea().equals("울산광역시")) {
                        spinner3.setSelection(7);
                    } else if (DataArea.getThirdArea().equals("강원도")) {
                        spinner3.setSelection(8);
                    } else if (DataArea.getThirdArea().equals("경기도")) {
                        spinner3.setSelection(9);
                    } else if (DataArea.getThirdArea().equals("경상북도")) {
                        spinner3.setSelection(10);
                    } else if (DataArea.getThirdArea().equals("경상남도")) {
                        spinner3.setSelection(11);
                    } else if (DataArea.getThirdArea().equals("전라북도")) {
                        spinner3.setSelection(12);
                    } else if (DataArea.getThirdArea().equals("전라남도")) {
                        spinner3.setSelection(13);
                    } else if (DataArea.getThirdArea().equals("충청북도")) {
                        spinner3.setSelection(14);
                    } else if (DataArea.getThirdArea().equals("충청남도")) {
                        spinner3.setSelection(15);
                    } else if (DataArea.getThirdArea().equals("제주특별자치도")) {
                        spinner3.setSelection(16);
                    }

                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });

                    Button confirmBt = (Button) dialogView.findViewById(R.id.main_category_area_dialog_confirm);
                    confirmBt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String firstArea = spinner1.getSelectedItem().toString();
                            String secondArea = spinner2.getSelectedItem().toString();
                            String thirdArea = spinner3.getSelectedItem().toString();

                            DataArea.setFirstArea(firstArea);
                            DataArea.setSecondArea(secondArea);
                            DataArea.setThirdArea(thirdArea);

                            dialog.dismiss();
                        }
                    });
                    Button cancelBt = (Button) dialogView.findViewById(R.id.main_category_area_dialog_cancel);
                    cancelBt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navView = navigationView.getHeaderView(0);

        mNavProfileImg = (ImageView) navView.findViewById(R.id.nav_profile_img);
        mNavNicknameTv = (TextView) navView.findViewById(R.id.nav_nickname_tv);

        // // TODO: 2017-10-28
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(false);

        TextView tv = (TextView) findViewById(R.id.main_my_tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        /*toggle.setHomeAsUpIndicator(R.drawable.my);

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        mSharedNickname = DataSharedPreference.getPreferences(this, DataSharedPreference.mNickNameTag);
        mNavNicknameTv.setText(mSharedNickname);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);

        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.drawer_notice) {
            Intent i = new Intent(this, NoticeActivity.class);
            startActivity(i);
        } else if (id == R.id.drawer_ad) {
            Intent i = new Intent(this, ManageAdActivity.class);
            startActivity(i);
        } else if (id == R.id.drawer_chat) {
            Intent i = new Intent(this, ChatActivity.class);
            startActivity(i);
        } else if (id == R.id.drawer_mypage) {
            Intent i = new Intent(this, MypageActivity.class);
            startActivity(i);
        } else if (id == R.id.drawer_setting) {
            Intent i = new Intent(this, SettingActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
