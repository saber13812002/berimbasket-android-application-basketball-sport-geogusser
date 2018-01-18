package ir.berimbasket.app.ui.intro;

import android.support.annotation.NonNull;

import com.cleveroad.slidingtutorial.Direction;
import com.cleveroad.slidingtutorial.PageSupportFragment;
import com.cleveroad.slidingtutorial.TransformItem;

import ir.berimbasket.app.R;

public class StadiumIntroFragment extends PageSupportFragment {

	@Override
	protected int getLayoutResId() {
		return R.layout.fragment_intro_stadium;
	}

	@NonNull
    @Override
	protected TransformItem[] getTransformItems() {
		return new TransformItem[]{
				TransformItem.create(R.id.imView_introStadiumBG, Direction.RIGHT_TO_LEFT, 0.2f),
				TransformItem.create(R.id.imView_introStadiumCenter, Direction.LEFT_TO_RIGHT, 0.6f),
				TransformItem.create(R.id.imView_introStadium1, Direction.RIGHT_TO_LEFT, 0.08f),
				TransformItem.create(R.id.imView_introStadium2, Direction.LEFT_TO_RIGHT, 0.1f),
				TransformItem.create(R.id.imView_introStadium3, Direction.LEFT_TO_RIGHT, 0.03f),
		};
	}
}
