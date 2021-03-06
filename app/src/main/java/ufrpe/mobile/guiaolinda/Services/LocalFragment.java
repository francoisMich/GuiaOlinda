package ufrpe.mobile.guiaolinda.Services;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.UUID;

import ufrpe.mobile.guiaolinda.DB.LocalLab;
import ufrpe.mobile.guiaolinda.GUI.activities.MapsActivity;
import ufrpe.mobile.guiaolinda.R;

public class LocalFragment extends Fragment {
    public static final String ARG_LOCAL_ID = "crime_id";

    private Local mLocal;
    private ImageButton mImgLocal;

    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;


    public static LocalFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOCAL_ID, crimeId);

        LocalFragment fragment = new LocalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID localId = (UUID) getArguments().getSerializable(ARG_LOCAL_ID);
        mLocal = LocalLab.get().getLocal(localId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_local, container, false);

        if (mLocal.getImagem() != R.drawable.semfoto) {
            mImgLocal = (ImageButton) v.findViewById(R.id.local_image);
            mImgLocal.setImageResource(mLocal.getImagem());
            mImgLocal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    zoomImageFromThumb(v, mImgLocal, mLocal.getImagem());
                }
            });
        } else {
            LinearLayout mLocalImgWrapper = (LinearLayout) v.findViewById(R.id.local_image_wrapper);
            mLocalImgWrapper.setVisibility(View.GONE);
        }

        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        TextView mNameField = (TextView) v.findViewById(R.id.local_name);
        if (!mLocal.get_nome_local().equals("")) {
            mNameField.setText(mLocal.get_nome_local().toLowerCase());
        } else {
            mNameField.setVisibility(View.GONE);
        }

/*        TextView mDescriptionField = (TextView) v.findViewById(R.id.descricao);
        if(!mLocal.getEndereco().equals("") || mLocal){
            mDescriptionField.setText("Descrição");
        }else {
            mDescriptionField.setVisibility(View.GONE);
        }*/

        TextView mAddressField = (TextView) v.findViewById(R.id.local_endereco);
        if (!mLocal.getEndereco().equals("")) {
            mAddressField.setText(String.format("     %s", mLocal.getEndereco()));
            mAddressField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.address, 0, 0, 0);
        } else {
            mAddressField.setVisibility(View.GONE);
        }

        TextView mTaxField = (TextView) v.findViewById(R.id.local_entrada);
        mTaxField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cash, 0, 0, 0);
        if (mLocal.getPreco() == null || mLocal.getPreco().equals("")) {
            mTaxField.setText("     Gratuito");
        } else {
            mTaxField.setText(String.format("     %s", mLocal.getPreco()));
        }

        TextView mFoneField = (TextView) v.findViewById(R.id.local_fone);
        if (!mLocal.getTelefone().equals("")) {
            mFoneField.setText(String.format("    %s", mLocal.getTelefone()));
            mFoneField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.phone, 0, 0, 0);
        } else {
            mFoneField.setVisibility(View.GONE);
        }

        TextView mFaxField = (TextView) v.findViewById(R.id.local_funcionamento);
        if (!mLocal.getHorario().equals("")) {
            mFaxField.setText(String.format("    %s", mLocal.getHorario()));
            mFaxField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.time, 0, 0, 0);
        } else {
            mFaxField.setVisibility(View.GONE);
        }

        TextView mSiteField = (TextView) v.findViewById(R.id.local_site);
        if (!mLocal.getSite().equals("")) {
            mSiteField.setText(String.format("    %s", mLocal.getSite()));
            mSiteField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.site, 0, 0, 0);
        } else {
            mSiteField.setVisibility(View.GONE);
        }

        TextView mEmailField = (TextView) v.findViewById(R.id.local_email);
        if (!mLocal.getEmail().equals("")) {
            mEmailField.setText(String.format("   %s", mLocal.getEmail()));
            mEmailField.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, 0, 0);
        } else {
            mEmailField.setVisibility(View.GONE);
        }

        Button goToMaps = (Button) v.findViewById(R.id.go_to_maps);
        goToMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocalFragment.this.getActivity(), MapsActivity.class);
                intent.putExtra(ARG_LOCAL_ID, mLocal.getId());
                startActivity(intent);
            }
        });

        return v;
    }

    private void zoomImageFromThumb(View v, final ImageButton mImgLocal, int imagem) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
        final ImageView expandedImageView = (ImageView) v.findViewById(R.id.expanded_image);
        expandedImageView.setImageResource(imagem);

        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        mImgLocal.getGlobalVisibleRect(startBounds);
        v.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        mImgLocal.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();


        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y, startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mImgLocal.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        mImgLocal.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });

    }
}
