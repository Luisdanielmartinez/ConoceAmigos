package com.example.adivina;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adivina.fragment.ChatFragment;
import com.example.adivina.fragment.ContactosFragment;
import com.example.adivina.models.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.imv_perfil)
    ImageView mImv_perfil;
    @BindView(R.id.tv_nombre_usuario)
    TextView mTv_nombre_usuario;
    @BindView(R.id.tooball_userInfo)
    Toolbar toolbar;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private  AppCompatActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        TabLayout tabLayout=findViewById(R.id.tableYout_userInfo_page);
        ViewPager viewPager=findViewById(R.id.viewPager);

        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario=dataSnapshot.getValue(Usuario.class);
                mTv_nombre_usuario.setText(usuario.getUserName());
                if(usuario.getImagenUrl().equals("default")){
                    mImv_perfil.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(getBaseContext()).load(usuario.getImagenUrl()).into(mImv_perfil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ViewPageAdater viewPageAdater=new ViewPageAdater(getSupportFragmentManager());
        viewPageAdater.addFragments(new ChatFragment(),"Chat");
        viewPageAdater.addFragments(new ContactosFragment(),"Contactos");
        viewPager.setAdapter(viewPageAdater);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);;
        return true;
    }

    //aqui va el menu de la informacion del usuario
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.perfil:
                 intent=new Intent(HomeActivity.this,PerfilActivity.class);
                 startActivity(intent);
               return true;
        }
        return false;
    }



    class ViewPageAdater extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPageAdater(FragmentManager fm){
            super(fm);
            this.fragments=new ArrayList<>();
            this.titles=new ArrayList<>();
        }
        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        public void addFragments(Fragment fragment,String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

}
