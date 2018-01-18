package ir.berimbasket.app.ui.intro;

import android.support.annotation.NonNull;

import com.cleveroad.slidingtutorial.Direction;
import com.cleveroad.slidingtutorial.PageSupportFragment;
import com.cleveroad.slidingtutorial.TransformItem;

import ir.berimbasket.app.R;

public class MatchIntroFragment extends PageSupportFragment {

	@Override
	protected int getLayoutResId() {
		return R.layout.fragment_intro_match;
	}

	@NonNull
    @Override
	protected TransformItem[] getTransformItems() {
		return new TransformItem[]{
				TransformItem.create(R.id.imView_introMatchBG, Direction.RIGHT_TO_LEFT, 0.2f),
				TransformItem.create(R.id.imView_introMatchCenter, Direction.LEFT_TO_RIGHT, 0.06f),
				TransformItem.create(R.id.imView_introMatch1, Direction.RIGHT_TO_LEFT, 0.08f),
				TransformItem.create(R.id.imView_introMatch2, Direction.LEFT_TO_RIGHT, 0.1f),
				TransformItem.create(R.id.imView_introMatch3, Direction.LEFT_TO_RIGHT, 0.03f),
		};
	}
}