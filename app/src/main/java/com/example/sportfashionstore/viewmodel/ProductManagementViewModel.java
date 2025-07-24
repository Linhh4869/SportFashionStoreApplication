package com.example.sportfashionstore.viewmodel;

import static com.example.sportfashionstore.util.Constants.CREATE;
import static com.example.sportfashionstore.util.Constants.DELETE;
import static com.example.sportfashionstore.util.Constants.UPDATE;

import androidx.lifecycle.MutableLiveData;

import com.example.sportfashionstore.app.MyApplication;
import com.example.sportfashionstore.callback.DataStateCallback;
import com.example.sportfashionstore.commonbase.BaseViewModel;
import com.example.sportfashionstore.commonbase.Resource;
import com.example.sportfashionstore.commonbase.SingleLiveData;
import com.example.sportfashionstore.model.Category;
import com.example.sportfashionstore.model.Product;
import com.example.sportfashionstore.model.ProductVariant;
import com.example.sportfashionstore.model.SubmitVariant;
import com.example.sportfashionstore.repository.ProductManagementRepository;
import com.example.sportfashionstore.util.Helper;
import com.example.sportfashionstore.util.SharePrefHelper;
import com.google.firebase.Timestamp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ProductManagementViewModel extends BaseViewModel {
    private final ProductManagementRepository productMnRepo;
    private final SharePrefHelper sharePrefHelper;
    public static final String ALL_PRODUCT = "";
    private MutableLiveData<String> userName = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Category>> categoryList = new MutableLiveData<>();
    private MutableLiveData<Resource<List<Product>>> productsLiveData = new MutableLiveData<>();
    private MutableLiveData<Resource<Product>> productLiveData = new MutableLiveData<>();
    private MutableLiveData<List<ProductVariant>> variantLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> price = new MutableLiveData<>(0);
    private MutableLiveData<Integer> salePrice = new MutableLiveData<>(0);
    private MutableLiveData<Category> selectedCategory = new MutableLiveData<>();
    private MutableLiveData<String> descProduct = new MutableLiveData<>();
    private MutableLiveData<String> colorVariant = new MutableLiveData<>();
    private MutableLiveData<Integer> invVariant = new MutableLiveData<>();
    private MutableLiveData<List<String>> sizeList = new MutableLiveData<>();
    private MutableLiveData<String> urlImage = new MutableLiveData<>();
    private MutableLiveData<Product> submitProduct = new MutableLiveData<>(new Product());
    private MutableLiveData<List<ProductVariant>> submitVariants = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Resource<List<ProductVariant>>> dynamicVariants = new MutableLiveData<>();
    private SingleLiveData<String> onCURDVariant = new SingleLiveData<>();
    private SingleLiveData<String> errorSubmitProduct = new SingleLiveData<>();
    private SingleLiveData<String> onSubmitProduct = new SingleLiveData<>();

    public ProductManagementViewModel() {
        productMnRepo = new ProductManagementRepository();
        sharePrefHelper = MyApplication.getSharePrefHelper();
        userName.setValue(MyApplication.getSharePrefHelper().getUserName());
    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public void setUserName(MutableLiveData<String> userName) {
        this.userName = userName;
    }

    public void getAllCategoryList() {
        setLoadingState(productsLiveData);
        productMnRepo.getCategoryList(new DataStateCallback<>() {
            @Override
            public void onSuccess(ArrayList<Category> data) {
                categoryList.setValue(data);
                sharePrefHelper.setCategory(new Gson().toJson(data));
                getProductsByCategory(ALL_PRODUCT);

            }

            @Override
            public void onError(String message) {
                setErrorState(productsLiveData, message);
            }
        });
    }

    public void getProductsByCategory(String categoryId) {
        setLoadingState(productsLiveData);
        productMnRepo.getProductListByCategory(categoryId, new DataStateCallback<>() {
            @Override
            public void onSuccess(List<Product> data) {
                setSuccessState(productsLiveData, data);
            }

            @Override
            public void onError(String message) {
                setErrorState(productsLiveData, message);
            }
        });
    }

    public void getProductToCURD(String productId) {
        setLoadingState(productLiveData);
        productMnRepo.getProductToCURD(productId, new DataStateCallback<>() {
            @Override
            public void onSuccess(Product data) {
                data.setId(productId);
                setSuccessState(productLiveData, data);
                submitProduct.setValue(data);
                submitVariants.setValue(data.getProductVariants());
                descProduct.setValue(data.getDescription());
                setSuccessState(dynamicVariants, submitVariants.getValue());
            }

            @Override
            public void onError(String message) {
                setErrorState(productLiveData, message);
            }
        });
    }

    private List<Category> getMyCategoryList() {
        List<Category> list;
        String categoriesJson = sharePrefHelper.getCategory();
        try {
            return new Gson().fromJson(categoriesJson,  new TypeToken<List<Category>>() {}.getType());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private final DataStateCallback<String> callbackSubmitProduct = new DataStateCallback<>() {
        @Override
        public void onSuccess(String data) {
            onSubmitProduct.setValue(data);
        }

        @Override
        public void onError(String message) {
            errorSubmitProduct.setValue(message);
        }
    };

    public void onCreateNewProduct() {
        Product submitPro = submitProduct.getValue();
        if (isValidSubmitProduct() && submitPro != null) {
            submitPro.setRating(getRandomRating());
            submitPro.setSold(getRandomSold());
            productMnRepo.addProduct(submitPro, callbackSubmitProduct);
        }
    }

    public void onUpdateProduct() {
        if (isValidSubmitProduct() && submitProduct.getValue() != null) {
            productMnRepo.updateProduct(submitProduct.getValue(), callbackSubmitProduct);
        }
    }

    private boolean isValidSubmitProduct() {
        Product submitPro = submitProduct.getValue();;
        if (submitPro == null)
            return false;

        submitPro.setDescription(getDescProduct().getValue());
        submitPro.setProductVariants(submitVariants.getValue());

        if (submitPro.getDescription().isEmpty()) {
            errorSubmitProduct.setValue("Không được để trống mô tả sản phẩm!");
            return false;
        }

        if (submitPro.getPrice() <= 0) {
            errorSubmitProduct.setValue("Giá sản phẩm không được để trống");
            return false;
        }

        if (submitPro.getSalePrice() >= submitPro.getPrice()) {
            errorSubmitProduct.setValue("Giá khuyến mại không hợp lệ");
            return false;
        }

        if (submitPro.getProductVariants().isEmpty()) {
            errorSubmitProduct.setValue("Cần tạo ít nhất một mẫu sản phẩm");
            return false;
        }

        return true;
    }

    private String getRandomRating() {
        Random random = new Random();
        int min = 31; // tương đương 3.1
        int max = 49; // tương đương 4.9
        int randomInt = random.nextInt(max - min + 1) + min;
        return String.valueOf(randomInt / 10f);
    }

    public String getRandomSold() {
        return String.valueOf(new Random().nextInt(100) + 1);
    }

    public List<String> getCategoryString() {
        List<Category> mCate = getMyCategoryList();
        if (mCate.isEmpty()) return new ArrayList<>();

        List<String> categories = new ArrayList<>();
        for (Category category : mCate) {
            if (!category.getName().equals("Tất cả")) {
                categories.add(category.getName());
            }
        }

        return categories;
    }

    public void setCategoryProduct(int position) {
        if (getMyCategoryList().isEmpty() || submitProduct == null || submitProduct.getValue() == null)
            return;

        List<Category> categories = getMyCategoryList();
        submitProduct.getValue().setCategoryId(categories.get(position + 1).getId());
    }

    public int getCateGorySelected(String cateId) {
        if (getMyCategoryList().isEmpty())
            return 0;

        for (Category category : getMyCategoryList()) {
            if (category.getId().equals(cateId)) {
                return category.getIndex();
            }
        }

        return -1;
    }

    public void onDiscountSelected(int discountPercent) {
        if (getPrice() == null || getPrice().getValue() == null) return;
        int price = getPrice().getValue();
        setSalePriceDisplay(String.valueOf(price * discountPercent));
    }

    public void curdVariantTypeAddNew(int type, int position, String variantJson) {
        if (submitVariants == null || submitVariants.getValue() == null)
            return;

        ProductVariant variant = Helper.jsonToObject(variantJson, ProductVariant.class);

        switch (type) {
            case CREATE:
                if (variant != null) {
                    LinkedList<ProductVariant> linkedList = new LinkedList<>(submitVariants.getValue());
                    linkedList.addFirst(variant);
                    submitVariants.setValue(linkedList);
                }
                break;

            case UPDATE:
                if (variant != null) {
                    List<ProductVariant> linkedUpdateList = new ArrayList<>(submitVariants.getValue());
                    linkedUpdateList.set(position, variant);
                    submitVariants.setValue(linkedUpdateList);
                }
                break;

            case DELETE:
                submitVariants.getValue().remove(position);
                break;
        }
        setSuccessState(dynamicVariants, submitVariants.getValue());
    }

    public void curdVariantTypeUpdate(int type, ProductVariant variant) {
        if (submitProduct.getValue() == null)
            return;

        setLoadingState(dynamicVariants);
        variant.setStatus("active");
        variant.setProductId(submitProduct.getValue().getId());
        SubmitVariant submitVariant = variant.getSubmitVariant();
        submitVariant.setUpdateAt(Timestamp.now());
        switch (type) {
            case CREATE:
                submitVariant.setCreatedAt(Timestamp.now());
                addNewVariant(submitVariant);
                break;

            case UPDATE:
                updateVariant(submitVariant);
                break;

            case DELETE:
                deleteVariant(variant.getId());
                break;
        }
    }

    public void updateVariantListFromFirestore() {
        if (submitProduct == null || submitProduct.getValue() == null || submitProduct.getValue().getId().isEmpty())
            return;

        setLoadingState(dynamicVariants);
        productMnRepo.getVariantList(submitProduct.getValue().getId(), new DataStateCallback<>() {
            @Override
            public void onSuccess(List<ProductVariant> data) {
                if (!data.isEmpty()) {
                    submitVariants.setValue(data);
                    setSuccessState(dynamicVariants, submitVariants.getValue());
                } else {
                    setErrorState(dynamicVariants, "");
                }
            }

            @Override
            public void onError(String message) {
                setErrorState(dynamicVariants, message);
            }
        });
    }

    private final DataStateCallback<String> callbackCurdVariant = new DataStateCallback<>() {
        @Override
        public void onSuccess(String data) {
            updateVariantListFromFirestore();
        }

        @Override
        public void onError(String message) {
            onCURDVariant.postValue(message);
        }
    };

    public void addNewVariant(SubmitVariant variant) {
        if (variant == null) {
            onCURDVariant.postValue("Mau hang khong ton tai!");
            return;
        }

        productMnRepo.createVariant(variant, callbackCurdVariant);
    }

    public void updateVariant(SubmitVariant variant) {
        if (variant == null || variant.getId().isEmpty()) {
            onCURDVariant.postValue("Mau hang khong ton tai!");
            return;
        }

        productMnRepo.updateVariant(variant, callbackCurdVariant);
    }

    public void deleteVariant(String variantId) {
        if (variantId == null || variantId.isEmpty()) {
            onCURDVariant.postValue("Mau hang khong ton tai!");
            return;
        }

        productMnRepo.deleteVariant(variantId, callbackCurdVariant);
    }

    public MutableLiveData<ArrayList<Category>> getCategoryList() {
        return categoryList;
    }

    public MutableLiveData<Resource<List<Product>>> getProductsLiveData() {
        return productsLiveData;
    }

    public MutableLiveData<Resource<Product>> getProductLiveData() {
        return productLiveData;
    }

    public MutableLiveData<List<ProductVariant>> getVariantLiveData() {
        return variantLiveData;
    }

    public void setVariantLiveData(List<ProductVariant> variantLiveData) {
        this.variantLiveData.setValue(variantLiveData);
    }

    public MutableLiveData<String> getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(MutableLiveData<String> urlImage) {
        this.urlImage = urlImage;
    }

    public MutableLiveData<List<String>> getSizeList() {
        return sizeList;
    }

    public void setSizeList(MutableLiveData<List<String>> sizeList) {
        this.sizeList = sizeList;
    }

    public MutableLiveData<Integer> getInvVariant() {
        return invVariant;
    }

    public void setInvVariant(MutableLiveData<Integer> invVariant) {
        this.invVariant = invVariant;
    }

    public MutableLiveData<String> getColorVariant() {
        return colorVariant;
    }

    public void setColorVariant(MutableLiveData<String> colorVariant) {
        this.colorVariant = colorVariant;
    }

    public MutableLiveData<String> getDescProduct() {
        return descProduct;
    }

    public MutableLiveData<Category> getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(MutableLiveData<Category> selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public MutableLiveData<Integer> getSalePrice() {
        return salePrice;
    }

    public void setSalePriceDisplay(Integer salePrice) {
        this.salePrice.postValue(salePrice);
    }

    public MutableLiveData<Integer> getPrice() {
        return price;
    }

    public String getPriceDisplay() {
        if (price.getValue() == null) return "";
        return String.valueOf(price.getValue());
    }

    public void setPriceDisplay(String price) {
        if (submitProduct.getValue() == null || price == null || price.isEmpty())
            return;

        int mPrice = 0;
        try {
            mPrice = Integer.parseInt(price);
        } catch (Exception e) {
            mPrice = 0;
        }

        submitProduct.getValue().setPrice(mPrice);
    }

    public void setPrice(MutableLiveData<Integer> price) {
        this.price = price;
    }

    public String getSalePriceDisplay() {
        return String.valueOf(salePrice.getValue());
    }

    public void setSalePriceDisplay(String price) {
        if (submitProduct == null || submitProduct.getValue() == null|| price == null || price.isEmpty())
            return;

        int mPrice = 0;
        try {
            mPrice = Integer.parseInt(price);
        } catch (Exception e) {
            mPrice = 0;
        }

        submitProduct.getValue().setSalePrice(mPrice);
    }

    public MutableLiveData<Product> getSubmitProduct() {
        return submitProduct;
    }

    public List<ProductVariant> getSubmitVariants() {
        return submitVariants.getValue();
    }

    public MutableLiveData<Resource<List<ProductVariant>>> getDynamicVariants() {
        return dynamicVariants;
    }

    public SingleLiveData<String> getOnCURDVariant() {
        return onCURDVariant;
    }

    public SingleLiveData<String> getErrorSubmitProduct() {
        return errorSubmitProduct;
    }

    public SingleLiveData<String> getOnSubmitProduct() {
        return onSubmitProduct;
    }
}
