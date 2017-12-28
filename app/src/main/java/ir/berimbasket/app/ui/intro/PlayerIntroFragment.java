package ir.berimbasket.app.ui.intro;

import android.support.annotation.NonNull;

import com.cleveroad.slidingtutorial.Direction;
import com.cleveroad.slidingtutorial.PageFragment;
import com.cleveroad.slidingtutorial.TransformItem;

import ir.berimbasket.app.R;

public class PlayerIntroFragment extends PageFragment {

	@Override
	protected int getLayoutResId() {
		return R.layout.fragment_intro_player;
	}

	@NonNull
    @Override
	protected TransformItem[] getTransformItems() {
		return new TransformItem[]{
				TransformItem.create(R.id.imView_introPlayerBG, Direction.LEFT_TO_RIGHT, 0.2f),
				TransformItem.create(R.id.imView_introPlayerCenter, Direction.RIGHT_TO_LEFT, 0.06f),
				TransformItem.create(R.id.imView_introPlayer1, Direction.LEFT_TO_RIGHT, 0.08f),
				TransformItem.create(R.id.imView_introPlayer2, Direction.RIGHT_TO_LEFT, 0.1f),
				TransformItem.create(R.id.imView_introPlayer3, Direction.RIGHT_TO_LEFT, 0.03f),
		};
	}
}