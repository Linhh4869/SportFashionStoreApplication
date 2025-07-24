package com.example.sportfashionstore.imageupload;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sportfashionstore.viewmodel.CameraViewModel;

public class CameraActivity extends AppCompatActivity implements CameraManager.CameraCallback {
    private CameraManager cameraManager;
    private CameraViewModel viewModel;

    // UI Components
    private Button btnTakePhoto;
    private ImageView imgPreview;
    private Button btnRetake;
    private Button btnUseImage;
    private ProgressBar progressBar;
    private View layoutPreview;
    private View layoutInitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        initViews();
        initViewModel();
        initCameraManager();
        setupClickListeners();
        observeViewModel();

        // Start with taking photo
        openCamera();
    }

    private void initViews() {
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        imgPreview = findViewById(R.id.imgPreview);
        btnRetake = findViewById(R.id.btnRetake);
        btnUseImage = findViewById(R.id.btnUseImage);
        progressBar = findViewById(R.id.progressBar);
        layoutPreview = findViewById(R.id.layoutPreview);
        layoutInitial = findViewById(R.id.layoutInitial);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(CameraViewModel.class);
    }

    private void initCameraManager() {
        cameraManager = new CameraManager(this);
    }

    private void setupClickListeners() {
        btnTakePhoto.setOnClickListener(v -> openCamera());
        btnRetake.setOnClickListener(v -> {
            viewModel.clearCapturedImage();
            showInitialState();
            openCamera();
        });
        btnUseImage.setOnClickListener(v -> uploadImage());
    }

    private void observeViewModel() {
        viewModel.getCapturedImage().observe(this, imageFile -> {
            if (imageFile != null) {
                showImagePreview(imageFile);
            }
        });

        viewModel.getIsLoading().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            btnRetake.setEnabled(!isLoading);
            btnUseImage.setEnabled(!isLoading);
        });

        viewModel.getUploadResult().observe(this, result -> {
            if (result.isSuccess()) {
                Toast.makeText(this, "Upload thành công!", Toast.LENGTH_SHORT).show();
                returnImageUrl(result.getImageUrl());
            } else {
                Toast.makeText(this, "Lỗi: " + result.getErrorMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openCamera() {
        cameraManager.openCamera(this);
    }

    private void uploadImage() {
        File imageFile = viewModel.getCapturedImage().getValue();
        if (imageFile != null) {
            String outputDir = getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES).getAbsolutePath();
            viewModel.processAndUploadImage(imageFile, outputDir);
        }
    }

    private void showImagePreview(File imageFile) {
        try {
            // Load and display image
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            if (bitmap != null) {
                imgPreview.setImageBitmap(bitmap);
                showPreviewState();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Không thể hiển thị ảnh: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void showInitialState() {
        layoutInitial.setVisibility(View.VISIBLE);
        layoutPreview.setVisibility(View.GONE);
    }

    private void showPreviewState() {
        layoutInitial.setVisibility(View.GONE);
        layoutPreview.setVisibility(View.VISIBLE);
    }

    private void returnImageUrl(String imageUrl) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("image_url", imageUrl);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    // CameraManager.CameraCallback implementations
    @Override
    public void onPermissionDenied() {
        Toast.makeText(this, "Cần quyền camera để chụp ảnh", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onCameraError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onImageCaptured(File imageFile) {
        viewModel.setCapturedImage(imageFile);
    }

    // Handle activity results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cameraManager.handleActivityResult(requestCode, resultCode, data, this);
    }

    // Handle permission results
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraManager.handlePermissionResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up any remaining images
        viewModel.clearCapturedImage();
    }
}
