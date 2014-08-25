package net.uyghurdev.uyghurtextreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Text extends Activity {

	int margin, p = 0, height, fontSize, pageNum, totalPage = 1, partseperator;
	char a = 1600;
	String kashida = a + "";
	float kash;
	boolean reshape;
	boolean PreChapLastPage = false;
	boolean toRight = true;
//	boolean done = false;
	Line line;
	Page page;
	Canvas canvas;
	Paint paint;
	Bitmap newb;
	ImageView img;
	ImageView imgback;
	ProgressBar prog;
	private int currentpage = 0;
	Display display;
	Context otherAppsContext = null;
	private SharedPreferences getPreferences;
	boolean containsError = false;
	int frontPage, prePage, nextPage;
	int xDown, yDown, xTouch = 0, yTouch = 0, xClickOffset = 0,
			yClickOffset = 0, xOffset = 0, yOffset = 0, xCurrent, yCurrent,
			xUp, yUp;
	private WholeText text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.text);

		initalize();
		// String strPath=getIntent().getStringExtra("FilePath");

		showContent();

//		img.setImageDrawable(showPage(currentpage));
		img.setOnTouchListener(new ImageView.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				int tileSize = display.getWidth();
//				Log.d("Desplay", "screen width: " + tileSize);
				// TODO Auto-generated method stub

				// ---------------Action Down---------------------
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
//					Log.d("Action",
//							"down: " + event.getX() + "," + event.getY());
					xTouch = (int) event.getX();
					yTouch = (int) event.getY();
					xDown = (int) event.getX();
					yDown = (int) event.getY();
					xClickOffset = xTouch; // *****************
					yClickOffset = yTouch; // ****************

					// -----------------Action Move------------------------
				} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
//					Log.d("Action", "moved.");
					xOffset += xTouch - (int) event.getX();
					// offsets for scrolling the game board and other stuff
					// here, isn't related to clicking
					yOffset += yTouch - (int) event.getY();
					xCurrent = (int) event.getX();
					yCurrent = (int) event.getY();
					toRight = xCurrent - xTouch >= 0 ? true : false;
//					Log.d("Action Move", "moved." + xTouch + ", " + yDown
//							+ "  to  " + xCurrent + ", " + yCurrent
//							+ ".   Distance:" + (xCurrent - xTouch));
//					Log.d("toRight", "" + toRight);
					movePage();
					xTouch = xCurrent;
					yTouch = yCurrent;

					// ------------------Action Up----------------------------
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
	
					xUp = (int) event.getX();
					yUp = (int) event.getY();

					// ++++++++++++++Moved to Right, X scale > 1/4 Screen
					// Size+++++++++
					if (toRight) {
						if (xUp - xDown > tileSize / 4) {

							if (currentpage < text.getPageNum() - 1)// This is
							// not Last
							// Page,
							// Move to
							// Next Page
							{
//								Log.d("Up", "next page");
								currentpage++;
								showPageToRight();

							} else// This is Last Page
							{
								moveBack();
							}
						} else {
							moveBack();
						}

					} else {

						// ++++++++++++++Moved to Left, X scale > 1/4 Screen
						// Size+++++++++

//						Log.d("Up", (xUp - xDown) + ", " + (-tileSize / 4));
						if ((xUp - xDown) < -tileSize / 4) {
							if (currentpage == 0) // This is First Page
							{
								moveBack();
							}

							else // This is not First Page, Move to Previous
									// Page
							{
								currentpage--;
								showPageToLeft();
							}
						} else {
							moveBack();
						}

					}
					frontPage = 0;
					prePage = display.getWidth();
					nextPage = -display.getWidth();
				}
				return true;
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "").setIcon(R.drawable.file_open);
		menu.add(0, 1, 1, "").setIcon(R.drawable.setting);
		menu.add(0, 2, 2, "").setIcon(R.drawable.about);
		// menu.add(0, 3, 3, getString(R.string.MenuExit));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case 0:// Open File
			Intent newIntentOpen = new Intent();
			newIntentOpen.setClass(Text.this, Open.class);
			startActivity(newIntentOpen);
			finish();
			break;
		case 1:// Settings
			Intent newIntentSettings = new Intent();
			newIntentSettings.setClass(Text.this, Settings.class);
			startActivity(newIntentSettings);
			finish();
			break;
		case 2:// About
			AboutUyghurDev();
			break;
		case 3:// Exit
			finish();

			break;
		}
		return true;
	}

	private void AboutUyghurDev() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(this);
		final View textEntryView = inflater.inflate(R.layout.aboutdialog, null);
		final TextView title = (TextView) textEntryView
				.findViewById(R.id.about_title);
		final TextView txtlink = (TextView) textEntryView
				.findViewById(R.id.content);
		title.setTypeface(Configs.UIFont);
		txtlink.setTypeface(Configs.UIFont);
		title.setText(ArabicUtilities.reshape(getString(R.string.app_name)));
		txtlink.setText(ArabicUtilities
				.reshape(getString(R.string.About_content)));
		final AlertDialog.Builder builder = new AlertDialog.Builder(Text.this);
		builder.setCancelable(false);
		// builder.setIcon(R.drawable.icon);
		// builder.setTitle(ArabicUtilities.reshape("ئەپ ھەمبەھر"));
		builder.setView(textEntryView);
		builder.setNegativeButton(ArabicUtilities.reshape("OK"),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});
		builder.show();
	}

	private void initalize() {

		try {
			otherAppsContext = createPackageContext(
					"net.uyghurdev.uyghurtextreader", 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Intent intent = this.getIntent();
		if (intent.getStringExtra("FilePath") != null) {
			Configs.CurrentFile = intent.getStringExtra("Filepath");
		}

		getPreferences = otherAppsContext.getSharedPreferences(
				"UyghurTextReaderSettings", Context.MODE_PRIVATE);
		// Configs.TYPE_FACE = Typeface.createFromFile(getPreferences.getString(
		// "Font", "SystemFont"));
		Log.d("Font Path", getPreferences.getString("Font", "SystemFont"));
		text = new WholeText();
		margin = Integer.parseInt(getPreferences.getString("Margin", "10"));
		display = getWindowManager().getDefaultDisplay();
		frontPage = 0;
		prePage = -display.getWidth();
		nextPage = display.getWidth();
		img = (ImageView) findViewById(R.id.image);
		img.setPadding(margin, margin, margin, 0);
		imgback = (ImageView) findViewById(R.id.imageback);
		imgback.setPadding(margin, margin, margin, 0);
		prog = (ProgressBar)findViewById(R.id.progress);
		
		RelativeLayout bck = (RelativeLayout) findViewById(R.id.llparent);
		bck.setBackgroundColor(Color.parseColor(getPreferences.getString(
				"BackgroundColor", "WHITE")));
		fontSize = Integer.parseInt(getPreferences.getString("FontSize", "20"));
		partseperator = Integer.parseInt(getPreferences.getString(
				"PartSeperator", "20"));
		paint = new Paint();
		if(getPreferences.getString("Font", "SystemFont").equals("SystemFont"))
		{
		}else{
			if(new File(getPreferences.getString("Font", "SystemFont")).exists()){
				paint.setTypeface(Typeface.createFromFile(getPreferences.getString("Font", "SystemFont")));
			}else{
				Editor prefsPrivateEditor = getPreferences.edit();
				prefsPrivateEditor.putString("Font", "SystemFont");
				prefsPrivateEditor.putString("FontName", "SystemFont");
				prefsPrivateEditor.commit();
			}
			
		}
		paint.setColor(Color.parseColor(getPreferences.getString("FontColor",
				"BLACK")));
		paint.setTextSize(fontSize);
		height = getFontHeight(paint);
		paint.setAntiAlias(true);
		paint.setSubpixelText(true);
		paint.setTextAlign(Paint.Align.RIGHT);
		kash = getKashidaLength(paint, kashida);
		// fileName = Configs.FilePath;

	}

	private void showContent() {
		// TODO Auto-generated method stub
		
		final Handler handler = new Handler() {

			public void handleMessage(Message msg) {

				prog.setVisibility(View.GONE);

				img.setImageDrawable(showPage(0));
			}
		};

		Thread checkUpdate = new Thread() {
			public void run() {

				try {
					ReadText(Configs.CurrentFile);
					setPages();
//					done = true;
				} catch (Exception e) {
					// TODO Auto-generated catch block
//					Log.d("Exception", e.toString());
				}
				handler.sendEmptyMessage(0);
			}
		};

		checkUpdate.start();
	}

	// public static void readFully(InputStreamReader isr, char[] rC)
	// throws IOException {
	// int length = rC.length;
	// int read = 0;
	// do {
	// int res = isr.read(rC, read, length - read);
	// if (res > 0) {
	// read += res;
	// }
	// } while (read < length);
	// }

	private void ReadText(String strPath) {
		String txt = "";
		try {

			File f = new File(strPath);
			InputStream fileIS = new FileInputStream(f);
			InputStreamReader reader = new InputStreamReader(fileIS, "UTF-8");
			StringBuffer sb = new StringBuffer();
			int start = 0;
			int count;
			char[] buf = new char[1024];
			while ((count = reader.read(buf)) != -1) {
				sb.append(buf, start, count);
			}
			txt = sb.toString();

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		}
		boolean endOfText = false;
		while (!endOfText) {
			String prg = "";
			int part = txt.indexOf('\n');
			if (part == -1) {
				prg = txt;
				endOfText = true;
			} else {
				prg = txt.substring(0, part);
				txt = txt.substring(part + 1);
			}
			text.addParagraph(prg);

		}

	}

	private void setPages() {
		// TODO Auto-generated method stub
		page = new Page();
		int start = 0;
		int x = 0;
		int prg = 0;
		int y_axis = height;
		int x_axis;
		boolean hasMoreText = true;
		boolean firstLine = true;
		String para = text.getParagraph(prg);
		while (hasMoreText) {

			boolean endOfPar = false;
			int last;
			line = new Line();
			String lineString;
			int linelength;

			if (firstLine) {
				linelength = paint.breakText(ArabicUtilities.reshape(para),
						true, display.getWidth() - 2 * margin - fontSize, null);
			} else {
				linelength = paint.breakText(ArabicUtilities.reshape(para),
						true, display.getWidth() - 2 * margin, null);
			}

			if (linelength + 1 >= ArabicUtilities.reshape(para).length()) {
				lineString = para;
				last = start + linelength;
				endOfPar = true;
			} else if (para.substring(start, start + linelength + 1).endsWith(
					".")) {
				last = para.substring(start, start + linelength + 1)
						.lastIndexOf(".");
				lineString = para.substring(start, start + linelength + 1)
						.substring(0, last + 1);
			} else {
				last = para.substring(start, start + linelength + 1)
						.lastIndexOf(' ');

				if (last == -1) {
					last = linelength - 1;
					lineString = para.substring(start, start + linelength + 1)
							.substring(0, last) + "-";
				} else {
					lineString = para.substring(start, start + linelength + 1)
							.substring(0, last);
				}

			}
			int end = start + last;
			int kashnum;
			if (firstLine) {
				kashnum = (int) ((display.getWidth() - 2 * margin - fontSize - getStringLength(
						paint, lineString)) / kash);
			} else {
				kashnum = (int) ((display.getWidth() - 2 * margin - getStringLength(
						paint, lineString)) / kash);
			}

			if (endOfPar || prg == 0) {
				line.setKashNum(0);
			} else {
				line.setKashNum(kashnum);
			}
			BiDi bd = new BiDi();
			line.setParNum(prg);
			line.setStart(start);
			line.setEnd(end);
			line.setLineString(bd.reverse(lineString));
			if (prg == 0) {
				x_axis = (int) ((display.getWidth() + getStringLength(paint,
						lineString))) / 2 - margin;
			} else {
				x_axis = firstLine ? display.getWidth() - 2 * margin - fontSize
						: display.getWidth() - 2 * margin;
			}
			line.setXAxis(x_axis);
			line.setYAxis(y_axis);
			page.addLine(line);
			// start = end + 1;
			if (endOfPar) {
				y_axis += height + partseperator;
				prg++;
				start = 0;
				firstLine = true;
				if (prg < text.getParSize()) {
					para = text.getParagraph(prg);
				}
			} else {
				y_axis += height;
				firstLine = false;
				para = para.substring(last + 1);
			}

			if (y_axis + height / 2 > display.getHeight() - 2 * margin) {
				text.addPage(page);
				page = new Page();
				y_axis = height;
			}

			if (prg >= text.getParSize() - 1) {
				if (y_axis <= display.getHeight() - 2 * margin) {
					text.addPage(page);
				}
				hasMoreText = false;
			}
			x++;
//			Log.d("Break Line", "Current Line:" + x);
		}

	}

	private String reverse(String lineString) {
		// TODO Auto-generated method stub
		String line = "";
		for (int l = 0; l < lineString.length(); l++) {
			line += lineString.charAt(lineString.length() - l - 1);
		}
		return line;
	}

	private Drawable showPage(int crp) {
		// TODO Auto-generated method stub
		Page currentPage = text.getPage(crp);
		newb = Bitmap.createBitmap(display.getWidth() - 2 * margin,
				display.getHeight() - margin, Config.ARGB_8888);

		canvas = new Canvas(newb);

		for (int l = 0; l < currentPage.getPageLines().size(); l++) {
			String line = currentPage.getPageLines().get(l).getLineString();

			int n = 0;
			for (int k = 0; k < currentPage.getPageLines().get(l).getKashNum(); k++) {

				while (n < line.length() - 1) {
					if (
					// ((line.charAt(n) != ' '
					// && line.charAt(n) != '.'
					// && line.charAt(n) != '،'
					// && line.charAt(n) != '!'
					// && line.charAt(n) != '؟'
					// && line.charAt(n) != ':'
					// && (line.charAt(n + 1) == 'ى' || line.charAt(n + 1) ==
					// 'ې') || line.charAt(n + 1) == 'ي'))
					//
					// ||
					((line.charAt(n) == 'ن' || line.charAt(n) == 'ت'
							|| line.charAt(n) == 'ر' || line.charAt(n) == 'ز'
							|| line.charAt(n) == 'س' || line.charAt(n) == 'ش'
							|| line.charAt(n) == 'ب' || line.charAt(n) == 'پ'
							|| line.charAt(n) == 'ي' || line.charAt(n) == 'ث'
							|| line.charAt(n) == 'ح' || line.charAt(n) == 'ذ'
							|| line.charAt(n) == 'ص' || line.charAt(n) == 'ض'
							|| line.charAt(n) == 'ط' || line.charAt(n) == 'ظ'
							|| line.charAt(n) == 'ع' || line.charAt(n) == 'ج'
							|| line.charAt(n) == 'چ' || line.charAt(n) == 'خ'
							|| line.charAt(n) == 'غ' || line.charAt(n) == 'ق'
							|| line.charAt(n) == 'ف' || line.charAt(n) == 'د'
							|| line.charAt(n) == 'ھ' || line.charAt(n) == 'ۋ'
							|| line.charAt(n) == 'ژ' || line.charAt(n) == 'ل'
							|| line.charAt(n) == 'ك' || line.charAt(n) == 'گ'
							|| line.charAt(n) == 'ڭ' || line.charAt(n) == 1600) && (line
							.charAt(n + 1) == 'ن'
							|| line.charAt(n + 1) == 'ت'
							|| line.charAt(n + 1) == 'ث'
							|| line.charAt(n + 1) == 'ح'
							|| line.charAt(n + 1) == 'ص'
							|| line.charAt(n + 1) == 'ض'
							|| line.charAt(n + 1) == 'ط'
							|| line.charAt(n + 1) == 'ظ'
							|| line.charAt(n + 1) == 'ع'
							|| line.charAt(n + 1) == 'س'
							|| line.charAt(n + 1) == 'ش'
							|| line.charAt(n + 1) == 'ب'
							|| line.charAt(n + 1) == 'پ'
							|| line.charAt(n + 1) == 'ي'
							|| line.charAt(n + 1) == 'م'
							|| line.charAt(n + 1) == 'ج'
							|| line.charAt(n + 1) == 'چ'
							|| line.charAt(n + 1) == 'خ'
							|| line.charAt(n + 1) == 'غ'
							|| line.charAt(n + 1) == 'ق'
							|| line.charAt(n + 1) == 'ف'
							|| line.charAt(n + 1) == 'ھ'
							|| line.charAt(n + 1) == 'ل'
							|| line.charAt(n + 1) == 'ك'
							|| line.charAt(n + 1) == 'گ' || line.charAt(n + 1) == 'ڭ'))
							|| ((line.charAt(n) == 'ا' || line.charAt(n) == 'ە' || line
									.charAt(n) == 1600) && (line.charAt(n + 1) == 'ئ'
									|| line.charAt(n + 1) == 'ن'
									|| line.charAt(n + 1) == 'ت'
									|| line.charAt(n + 1) == 'س'
									|| line.charAt(n + 1) == 'ش'
									|| line.charAt(n + 1) == 'ب'
									|| line.charAt(n + 1) == 'پ'
									|| line.charAt(n + 1) == 'ي'
									|| line.charAt(n + 1) == 'م'
									|| line.charAt(n + 1) == 'ج'
									|| line.charAt(n + 1) == 'چ'
									|| line.charAt(n + 1) == 'خ'
									|| line.charAt(n + 1) == 'غ'
									|| line.charAt(n + 1) == 'ق'
									|| line.charAt(n + 1) == 'ف'
									|| line.charAt(n + 1) == 'ھ'
									|| line.charAt(n + 1) == 'ك'
									|| line.charAt(n + 1) == 'گ' || line
									.charAt(n + 1) == 'ڭ'))) {
						line = line.substring(0, n + 1) + kashida
								+ line.substring(n + 1);
						n += 2;
						break;
					}
					n++;
					if (n > line.length() - 2) {
						if (k == 0) {
							break;
						} else {
							n = 0;
						}
					}
				}

			}
			int x = currentPage.getPageLines().get(l).getXAxis();
			int h = currentPage.getPageLines().get(l).getYAxis();
			canvas.drawText(ArabicReshape.reshape_reverse(line), 0,
					ArabicReshape.reshape_reverse(line).length(), x, h, paint);
		}
		canvas.drawText((crp + 1) + "/" + text.getPageNum(),
				display.getWidth() / 2, display.getHeight() - 3 * margin / 2,
				paint);

		Drawable draw = new BitmapDrawable(newb);
		return draw;
	}

	private void showPageToLeft() {
		// TODO Auto-generated method stub
		imgback.setImageDrawable(showPage(currentpage + 1));
		Animation animback = new TranslateAnimation(xUp - xDown,
				-display.getWidth(), 0, 0);
		animback.setDuration(500 * (display.getWidth() + xUp - xDown)
				/ display.getWidth());
		animback.setFillAfter(true);
		imgback.startAnimation(animback);

		img.setImageDrawable(showPage(currentpage));
		Animation anim = new TranslateAnimation(display.getWidth() + xUp
				- xDown, 0, 0, 0);
		anim.setDuration(500 * (display.getWidth() + xUp - xDown)
				/ display.getWidth());
		anim.setFillAfter(true);
		img.startAnimation(anim);
	}

	private void showPageToRight() {
		// TODO Auto-generated method stub
		imgback.setImageDrawable(showPage(currentpage - 1));
		Animation animback = new TranslateAnimation(xUp - xDown,
				display.getWidth(), 0, 0);
		animback.setDuration(500 * (display.getWidth() - xUp + xDown)
				/ display.getWidth());
		animback.setFillAfter(true);
		imgback.startAnimation(animback);

		img.setImageDrawable(showPage(currentpage));
		Animation anim = new TranslateAnimation(-display.getWidth() + xUp
				- xDown, 0, 0, 0);
		anim.setDuration(500 * (display.getWidth() - xUp + xDown)
				/ display.getWidth());
		anim.setFillAfter(true);
		img.startAnimation(anim);
	}

	private void moveBack() {
		// TODO Auto-generated method stub
		if (xUp - xDown >= 0) {
			if (currentpage == text.getPageNum() - 1) {
				img.setImageDrawable(showPage(currentpage));
				Animation anim = new TranslateAnimation(xUp - xDown, 0, 0, 0);
				anim.setDuration(500 * (xUp - xDown) / display.getWidth());
				anim.setFillAfter(true);
				img.startAnimation(anim);
			} else {
				imgback.setImageDrawable(showPage(currentpage + 1));
				Animation animback = new TranslateAnimation(xUp - xDown
						- display.getWidth(), -display.getWidth(), 0, 0);
				animback.setDuration(500 * (xUp - xDown) / display.getWidth());
				animback.setFillAfter(true);
				imgback.startAnimation(animback);
				img.setImageDrawable(showPage(currentpage));
				Animation anim = new TranslateAnimation(xUp - xDown, 0, 0, 0);
				anim.setDuration(500 * (xUp - xDown) / display.getWidth());
				anim.setFillAfter(true);
				img.startAnimation(anim);

			}
		} else {
			if (currentpage == 0) {
				img.setImageDrawable(showPage(currentpage));
				Animation anim = new TranslateAnimation(xUp - xDown, 0, 0, 0);
				anim.setDuration(500 * (-xUp + xDown) / display.getWidth());
				anim.setFillAfter(true);
				img.startAnimation(anim);
			} else {
				imgback.setImageDrawable(showPage(currentpage - 1));
				Animation animback = new TranslateAnimation(xUp - xDown
						+ display.getWidth(), display.getWidth(), 0, 0);
				animback.setDuration(500 * (-xUp + xDown) / display.getWidth());
				animback.setFillAfter(true);
				imgback.startAnimation(animback);
				img.setImageDrawable(showPage(currentpage));
				Animation anim = new TranslateAnimation(xUp - xDown, 0, 0, 0);
				anim.setDuration(500 * (-xUp + xDown) / display.getWidth());
				anim.setFillAfter(true);
				img.startAnimation(anim);
			}
		}

	}

	private void movePage() {
		// TODO Auto-generated method stub

		if (xCurrent - xDown >= 0) {
//			Log.d("total page", "" + text.getPageNum());
//			Log.d("current page", "" + currentpage);
			if (currentpage < text.getPageNum() - 1) {
				Log.d("Process", "Done!");
				imgback.setImageDrawable(showPage(currentpage + 1));
				Animation animback = new TranslateAnimation(nextPage, nextPage
						+ xCurrent - xTouch, 0, 0);
				animback.setDuration(0);
				animback.setFillAfter(true);
				imgback.startAnimation(animback);
			}
			img.setImageDrawable(showPage(currentpage));
			Animation anim = new TranslateAnimation(frontPage, frontPage
					+ xCurrent - xTouch, 0, 0);
			anim.setDuration(0);
			anim.setFillAfter(true);
			img.startAnimation(anim);
		} else {
			if (currentpage > 0) {
				imgback.setImageDrawable(showPage(currentpage - 1));
				Animation animback = new TranslateAnimation(prePage, prePage
						+ xCurrent - xTouch, 0, 0);
				animback.setDuration(0);
				animback.setFillAfter(true);
				imgback.startAnimation(animback);
			}
			img.setImageDrawable(showPage(currentpage));
			Animation anim = new TranslateAnimation(frontPage, frontPage
					+ xCurrent - xTouch, 0, 0);
			anim.setDuration(0);
			anim.setFillAfter(true);
			img.startAnimation(anim);

		}
		frontPage += xCurrent - xTouch;
		prePage += xCurrent - xTouch;
		nextPage += xCurrent - xTouch;

	}

	public int getFontHeight(Paint mPaint) {
		FontMetrics fm = mPaint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.top) + 2;
	}

	public float getStringLength(Paint mPaint, String text) {

		return mPaint.measureText(ArabicUtilities.reshape(text));

	}

	
	public float getKashidaLength(Paint mPaint, String text) {

		return mPaint.measureText(text);

	}

}
