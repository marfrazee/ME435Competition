package edu.rosehulman.frazeema.integratedimagerec;

import android.os.Handler;
import android.widget.Toast;

/**
 * Created by frazeema on 4/24/2018.
 */

public class Scripts {
    private Handler mCommandHandler = new Handler();

    private GolfBallDeliveryActivity mActivity;

    private int ARM_REMOVAL_TIME = 8000;

    public Scripts(GolfBallDeliveryActivity activity) {
        mActivity = activity;
    }

    public void testStraightScript() {
        mActivity.sendCommand("DETACH 111111");
        mActivity.sendCommand("ATTACH 111111");
        mActivity.sendWheelSpeed(mActivity.mLeftStraightPwmValue, mActivity.mRightStraightPwmValue);
        Toast.makeText(mActivity,"Begin driving", Toast.LENGTH_SHORT).show();
        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.sendWheelSpeed(0,0);
                Toast.makeText(mActivity,"Stop driving", Toast.LENGTH_SHORT).show();
            }
        }, 8000);
    }
    public void nearBallScript() {
//        double distanceToNearBall  = NavUtils.getDistance(mActivity.mCurrentGpsX, mActivity.mCurrentGpsY ,NEAR_BALL_GPS_X, mActivity.mNearBallGpsY);
//        long driveTimeMs = (long) (distanceToNearBall/ RobotActivity.DEFAULT_SPEED_FT_PER_SEC * 1000);
        // FOR testing, this has been made shorter
//        driveTimeMs = 3000;
//        mActivity.sendWheelSpeed(mActivity.mLeftStraightPwmValue, mActivity.mRightStraightPwmValue);
        mActivity.sendWheelSpeed(0,0);
        removeBallAtLocationScript(mActivity.mNearBallLocation);

        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mActivity.mHighLevelState == GolfBallDeliveryActivity.State.NEAR_BALL_MISSION) {
                    mActivity.setState(GolfBallDeliveryActivity.State.FAR_BALL_MISSION);
                    mActivity.setSubstate(GolfBallDeliveryActivity.Substate.GPS_SEEKING);
                }
            }
        }, ARM_REMOVAL_TIME);
    }
    public void farBallScript() {
        mActivity.sendWheelSpeed(0,0);
        removeBallAtLocationScript(mActivity.mFarBallLocation);
        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActivity.sendWheelSpeed(0,0);
                if(mActivity.mWhiteBallLocation != 0) {
                    removeBallAtLocationScript(mActivity.mWhiteBallLocation);
                }
                if(mActivity.mHighLevelState == GolfBallDeliveryActivity.State.FAR_BALL_MISSION) {
                    mActivity.setState(GolfBallDeliveryActivity.State.HOME_CONE_MISSION);
                    mActivity.setSubstate(GolfBallDeliveryActivity.Substate.GPS_SEEKING);
                }
            }
        }, ARM_REMOVAL_TIME);

    }
    private void removeBallAtLocationScript(final int location) {
        mActivity.sendCommand("DETACH 111111");
        mActivity.sendCommand("ATTACH 111111");
        switch(location) {
            case 1:
                mActivity.sendCommand("GRIPPER 30");
                mCommandHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this, "Run script step 2", Toast.LENGTH_SHORT).show();
                        mActivity.sendCommand("POSITION 32 119 -83 -163 78");
//                sendCommand("WHEEL SPEED BRAKE 0 BRAKE 0");

                    }
                }, 3000);
                mCommandHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this, "Run script step 2", Toast.LENGTH_SHORT).show();
                        mActivity.sendCommand("POSITION 66 119 -83 -163 78");
//                sendCommand("WHEEL SPEED BRAKE 0 BRAKE 0");

                    }
                }, 6000);

                mCommandHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this, "Run script step 2", Toast.LENGTH_SHORT).show();
                        mActivity.sendCommand("POSITION 32 119 -83 -163 78");
//                sendCommand("WHEEL SPEED BRAKE 0 BRAKE 0");

                    }
                }, 9000);
                break;
            case 2:
                mActivity.sendCommand("POSITION 0 109 -72 -172 99");
                mCommandHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this, "Run script step 2", Toast.LENGTH_SHORT).show();
                        mActivity.sendCommand("POSITION 23 111 -72 -172 99");
//                sendCommand("WHEEL SPEED BRAKE 0 BRAKE 0");

                    }
                }, 3000);
                break;
            case 3:
                mActivity.sendCommand("POSITION 2 119 -83 -163 78");
                mCommandHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(MainActivity.this, "Run script step 2", Toast.LENGTH_SHORT).show();
                        mActivity.sendCommand("POSITION -46 119 -83 -163 78");
//                sendCommand("WHEEL SPEED BRAKE 0 BRAKE 0");

                    }
                }, 3000);

                break;
        }
        mCommandHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(MainActivity.this, "Run script step 2", Toast.LENGTH_SHORT).show();
                mActivity.sendCommand("POSITION 0 90 0 -90 90");
//                sendCommand("WHEEL SPEED BRAKE 0 BRAKE 0");

            }
        }, 8000);

//        mCommandHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mActivity.sendCommand("POSITION 83 90 0 -90 90");
//            }
//        }, 10);
//        mCommandHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mActivity.sendCommand("POSITION 90 141 -60 -180 169");
//            }
//        }, 2000);
//        mCommandHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mActivity.setLocationToColor(location, GolfBallDeliveryActivity.BallColor.NONE);
//            }
//        }, ARM_REMOVAL_TIME);
    }
}
