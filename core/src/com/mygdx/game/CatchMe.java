package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;

public class CatchMe extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img;
	Texture gameOverImg;
	Sprite sprite;
	Sprite gameOverSprite;
	BitmapFont font;
	String myText;
	GlyphLayout textLayout = new GlyphLayout();

	long timeCountDown;
	GlyphLayout countDownLayout = new GlyphLayout();
	GlyphLayout scoreLayout = new GlyphLayout();
	GlyphLayout bestScoreLayout = new GlyphLayout();

	int frame = 0;
	float coordinateX;
	float coordinateY;
	int clicksCount = 0;
	long lastClick = TimeUtils.millis();
	int ELAPSE_TIME = 15000;

	long bestScore = 0;
	long score = 0;

	int PADDING_SIZE = 10;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont(Gdx.files.internal("raavi_black.fnt"));
		myText = "LEVEL ";

		img = new Texture("location_pointer.png");
		gameOverImg = new Texture("game_over.png");
		sprite = new Sprite(img);
		gameOverSprite = new Sprite(gameOverImg);

		coordinateX = Gdx.graphics.getWidth() / 2 - img.getWidth() / 2;
		coordinateY = Gdx.graphics.getHeight() / 2 - img.getHeight() / 2;

		sprite.setPosition(
				coordinateX,
				coordinateY
		);

		gameOverSprite.setPosition(
				Gdx.graphics.getWidth() / 2 - gameOverImg.getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - gameOverImg.getHeight() / 2
		);

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {
		if (TimeUtils.millis() - lastClick > ELAPSE_TIME) {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			bestScore = bestScore > score ? bestScore : score;

			batch.begin();
			batch.draw(gameOverSprite, gameOverSprite.getX(), gameOverSprite.getY());
			batch.end();

		} else {
			Gdx.gl.glClearColor(1, 1, 1, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			batch.begin();

			textLayout.setText(font, myText + (clicksCount / 10 + 1));
			font.draw(batch, textLayout, Gdx.graphics.getWidth() / 2 - textLayout.width / 2, Gdx.graphics.getHeight() - PADDING_SIZE);

			timeCountDown = (ELAPSE_TIME - TimeUtils.millis() + lastClick) / 1000;
			countDownLayout.setText(font, "" + timeCountDown);
			font.draw(batch, countDownLayout, Gdx.graphics.getWidth() - PADDING_SIZE - countDownLayout.width, countDownLayout.height + PADDING_SIZE);

			scoreLayout.setText(font, "Score: " + score);
			font.draw(batch, scoreLayout, PADDING_SIZE, scoreLayout.height + PADDING_SIZE);

			bestScoreLayout.setText(font, "Best: " + bestScore);
			font.draw(batch, bestScoreLayout, scoreLayout.width + 2 * PADDING_SIZE, bestScoreLayout.height + PADDING_SIZE);

			if (frame % (90 - (clicksCount / 10) * 10) == 0) {
				coordinateX = MathUtils.random(0, Gdx.graphics.getWidth() - sprite.getWidth());
				coordinateY = MathUtils.random(
						countDownLayout.height + PADDING_SIZE,
						Gdx.graphics.getHeight() - sprite.getHeight() - textLayout.height - 2 * PADDING_SIZE
				);
			}

			batch.draw(sprite, coordinateX, coordinateY);

			frame++;

			batch.end();
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == Input.Buttons.LEFT) {

			// TODO: why "sprite.getX()" should be used instead of "coordinateX"?
			if (Gdx.graphics.getHeight() - screenY - coordinateY >= 0 && Gdx.graphics.getHeight() - screenY - coordinateY <= sprite.getHeight()
					&& screenX - coordinateX >= 0 && screenX - coordinateX <= sprite.getWidth()) {
				clicksCount++;
				score += timeCountDown;
				lastClick = TimeUtils.millis();
			}

		} else if (button == Input.Buttons.RIGHT) {
			frame = 0;
			score = 0;
			clicksCount = 0;
			lastClick = TimeUtils.millis();
			coordinateX = Gdx.graphics.getWidth() / 2 - img.getWidth() / 2;
			coordinateY = Gdx.graphics.getHeight() / 2 - img.getHeight() / 2;

			sprite.setPosition(
					coordinateX,
					coordinateY
			);
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
