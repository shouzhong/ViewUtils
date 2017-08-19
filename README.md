# ViewUtils
## 说明
一个简单的注解框架，功能包括布局绑定，控件绑定，控件的点击和长按时间。
## 使用
### 依赖
compile 'com.wuyifeng:ViewUtils:1.0.0'
### 代码
#### 在Activity中使用
```Java
@BindContent(R.layout.activity_sample)
public class SampleActivity extends AppCompatActivity {

    @BindView(R.id.tv)
    private TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
    }

    @OnClick(R.id.btn)
    private void onClick(View v) {
        tv.append("onClick->");
    }

    @OnLongClick(R.id.btn)
    boolean onLongClick(View v) {
        tv.append("onLongClick->");
        return true;
    }
}
```
#### 在Fragment中使用
```Java
@BindContent(R.layout.fragment_sample)
public class SampleFragment extends Fragment {

    @BindView(R.id.tv)
    private TextView tv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return ViewUtils.inject(this, inflater, container);
    }

    @OnClick(R.id.btn)
    private void onClick(View v) {
        tv.append("onClick->");
    }

    @OnLongClick(R.id.btn)
    private boolean onLongClick(View v) {
        tv.append("onLongClick->");
        return true;
    }
}
```
#### 在ViewHolder中使用
```Java
class RvHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv)
    TextView tv;

    public RvHolder(View itemView) {
        super(itemView);
        ViewUtils.inject(this, itemView);
    }
}
```
#### 在其它地方使用
可以参考Fragment和ViewHolder中的使用。
