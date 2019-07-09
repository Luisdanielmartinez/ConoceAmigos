package com.example.adivina;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adivina.fragment.LoginFragment;
import com.example.adivina.models.Usuario;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PerfilActivity extends AppCompatActivity {
    @BindView(R.id.tooball_perfil)
    Toolbar toolbar;
    @BindView(R.id.imv_perfil_perfil)
    ImageView mImv_perfil;
    @BindView(R.id.tv_perfil_nombre_usuario)
    TextView mTv_nombre_usuario;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private StorageTask storageTask;
    private Uri myurl;
    private static final int IMAGE_REQUEST=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Perfil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        storageReference= FirebaseStorage.getInstance().getReference("fotos");
        mImv_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(PerfilActivity.this);

            }
        });
        obtenerUsuario();

    }

    public void obtenerUsuario(){
        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario=dataSnapshot.getValue(Usuario.class);
                mTv_nombre_usuario.setText(usuario.getUserName());
                if (usuario.getImagenUrl().equals("default")){
                    mImv_perfil.setImageResource(R.mipmap.ic_launcher);
                }else{
                    Glide.with(getBaseContext()).load(usuario.getImagenUrl()).into(mImv_perfil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public String getdireccionArchivo(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void cargarImagen(){
        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();
        if(myurl!=null){
          final StorageReference fileReference=storageReference.child(System.currentTimeMillis()
          +"."+getdireccionArchivo(myurl));

          storageTask=fileReference.putFile(myurl);
          storageTask.continueWithTask(new Continuation() {
              @Override
              public Object then(@NonNull Task task) throws Exception {
                  if(!task.isComplete()){
                      throw task.getException();
                  }
                  return fileReference.getDownloadUrl();
              }
          }).addOnCompleteListener(new OnCompleteListener<Uri>() {
              @Override
              public void onComplete(@NonNull Task<Uri> task) {
                  if(task.isSuccessful()){
                      Uri uri=task.getResult();
                      String imageurl=uri.toString();

                      databaseReference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                      HashMap<String, Object> hashMap=new HashMap<>();

                      hashMap.put("imagenUrl",imageurl);
                      databaseReference.updateChildren(hashMap);
                      progressDialog.dismiss();
                  }else{
                      Toast.makeText(getApplicationContext(),"Error en guardar",Toast.LENGTH_LONG).show();
                      progressDialog.dismiss();
                  }
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {
                  Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                  progressDialog.dismiss();
              }
          });
        }else{
            Toast.makeText(getApplicationContext(),"No se puede seleccionar la imagen",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            myurl=result.getUri();
            cargarImagen();

        }else{
            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
        }
    }
}
