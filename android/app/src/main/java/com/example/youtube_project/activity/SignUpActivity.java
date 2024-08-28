package com.example.youtube_project.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.youtube_project.ApiHandler;
import com.example.youtube_project.R;
import com.example.youtube_project.home.HomeScreenActivity;
import com.example.youtube_project.user.User;
import com.example.youtube_project.user.UserManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private EditText usernameEditText;
    private EditText nicknameEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private TextView usernameErrorTextView;
    private TextView nicknameErrorTextView;
    private TextView passwordErrorTextView;
    private TextView confirmPasswordErrorTextView;
    private TextView photoErrorTextView;
    private ImageView profileImageView;
    private ToggleButton togglePasswordButton;
    private ToggleButton toggleConfirmPasswordButton;
    private Uri photoUri;

    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameEditText = findViewById(R.id.username);
        nicknameEditText = findViewById(R.id.nickname);
        passwordEditText = findViewById(R.id.password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        usernameErrorTextView = findViewById(R.id.username_error);
        nicknameErrorTextView = findViewById(R.id.nickname_error);
        passwordErrorTextView = findViewById(R.id.password_error);
        confirmPasswordErrorTextView = findViewById(R.id.confirm_password_error);
        photoErrorTextView = findViewById(R.id.photo_error);
        profileImageView = findViewById(R.id.profile_image);
        Button uploadPhotoButton = findViewById(R.id.upload_photo_button);
        Button signUpButton = findViewById(R.id.sign_up_button);
        togglePasswordButton = findViewById(R.id.toggle_password);
        toggleConfirmPasswordButton = findViewById(R.id.toggle_confirm_password);

        userManager = UserManager.getInstance();

        // Request necessary permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
        }

        // Toggle password visibility
        togglePasswordButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        toggleConfirmPasswordButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                confirmPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                confirmPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        uploadPhotoButton.setOnClickListener(v -> showPhotoOptions());

        signUpButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String nickname = nicknameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            boolean isValid = true;

            if (!validateUsername(username)) {
                usernameErrorTextView.setText(R.string.username_error_message);
                isValid = false;
            } else {
                usernameErrorTextView.setText("");
            }

            if (!validateNickname(nickname)) {
                nicknameErrorTextView.setText(R.string.nickname_error_message);
                isValid = false;
            } else {
                nicknameErrorTextView.setText("");
            }

            if (!validatePassword(password)) {
                passwordErrorTextView.setText(R.string.password_error_message);
                isValid = false;
            } else {
                passwordErrorTextView.setText("");
            }

            if (!password.equals(confirmPassword)) {
                confirmPasswordErrorTextView.setText(R.string.confirm_password_error_message);
                isValid = false;
            } else {
                confirmPasswordErrorTextView.setText("");
            }

            if (photoUri == null) {
                photoErrorTextView.setText("Photo is required");
                isValid = false;
            } else {
                photoErrorTextView.setText("");
            }

            if (isValid) {
                    performRegistration(username, nickname, password);
            }
        });

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void performRegistration(String username, String nickname, String password) {
        new Thread(() -> {
            try {
                File imageFile = new File(photoUri.getPath());

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("username", username)
                        .addFormDataPart("displayName", nickname)
                        .addFormDataPart("password", password)
                        .addFormDataPart("photo", imageFile.getName(),
                                RequestBody.create(MediaType.parse("image/*"), imageFile))
                        .build();

                Response response = ApiHandler.sendMultipartPost("/auth/register", requestBody);

                if (response.isSuccessful()) {


                        runOnUiThread(() -> {
                            Toast.makeText(SignUpActivity.this, R.string.user_registered_successfully, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });

                } else {
                    runOnUiThread(() -> Toast.makeText(SignUpActivity.this, response.message(), Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Error occurred, please try again later." , Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void showPhotoOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Upload Photo");
        builder.setItems(new CharSequence[]{"Take Photo", "Choose from Gallery"}, (dialog, which) -> {
            if (which == 0) {
                takePhoto();
            } else if (which == 1) {
                selectImage();
            }
        });
        builder.show();
    }

    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, "com.example.youtube_project.fileprovider", photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "Error occurred while creating the file", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No Camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK) {
                if (data != null) {
                    Uri selectedImageUri = data.getData();
                    photoUri = saveImageToInternalStorage(selectedImageUri);
                    profileImageView.setImageURI(photoUri);
                }
            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                profileImageView.setImageURI(photoUri);
            }
        }
    }


    private boolean validateUsername(String username) {
        return username.length() >= 5 && username.matches(".*[a-zA-Z].*") && username.matches(".*\\d.*");
    }
    private boolean validateNickname(String nickname) {
        return nickname.length() >= 1 && nickname.matches("[a-zA-Z0-9]*");
    }


    private boolean validatePassword(String password) {
        return password.length() >= 8 && password.matches(".*[a-zA-Z].*") && password.matches(".*\\d.*");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
            } else {
                Toast.makeText(this, "Camera and storage permissions are required to take a photo", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private Uri saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            bitmap = rotateImageIfRequired(bitmap, imageUri);
            File file = new File(getFilesDir(), "profile_image.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
            return Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {
        InputStream input = getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            ei = new ExifInterface(input);
        } else {
            ei = new ExifInterface(selectedImage.getPath());
        }

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }


}
