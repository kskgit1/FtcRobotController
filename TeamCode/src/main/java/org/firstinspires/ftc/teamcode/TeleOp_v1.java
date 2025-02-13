package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="TeleOp_v1", group="Linear Opmode")
public class TeleOp_v1 extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();

    //name motor variables
    private DcMotor fldrive, frdrive, brdrive, bldrive, slides, arm;
    private Servo gripper;
    double  MIN_POSITION = 0, MAX_POSITION = 1;
    double gripPosition;

    @Override
    public void runOpMode()
    {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //map the motor variables to actual motors
        fldrive = hardwareMap.get(DcMotor.class, "fldrive");
        frdrive = hardwareMap.get(DcMotor.class, "frdrive");
        brdrive = hardwareMap.get(DcMotor.class, "brdrive");
        bldrive = hardwareMap.get(DcMotor.class, "bldrive");

        slides = hardwareMap.get(DcMotor.class, "slides");
        arm = hardwareMap.get(DcMotor.class, "arm");
        
        gripper = hardwareMap.servo.get("gripper);

        //set direction of motors
        fldrive.setDirection(DcMotor.Direction.REVERSE);
        frdrive.setDirection(DcMotor.Direction.FORWARD);
        brdrive.setDirection(DcMotor.Direction.FORWARD);
        bldrive.setDirection(DcMotor.Direction.REVERSE);

        slides.setDirection(DcMotor.Direction.FORWARD);
        arm.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        
        gripPosition = MAX_POSITION;

        while (opModeIsActive())
        {
            // motion controls
            double speed = gamepad1.left_stick_y;
            double turn = -gamepad1.right_stick_x;
            double strafe = -gamepad1.left_stick_x;

            // slides controls
            double slidesup = gamepad1.right_trigger;
            double slidesdown = gamepad1.left_trigger;

            //determine power for each motor
            double fl = speed + turn + strafe;
            double fr = speed - turn - strafe;
            double br = speed - turn + strafe;
            double bl = speed + turn - strafe;

            double s = slidesup - slidesdown;
            double a = 0.5;

            

            if(gamepad1.y)
            {
                arm.setPower(Math.abs(a));
            }

            // open the gripper on X button if not already at most open position.
            if (gamepad1.b && gripPosition < MAX_POSITION) 
            {
                gripPosition = gripPosition + .01;
            }

            // close the gripper on Y button if not already at the closed position.
            if (gamepad1.a && gripPosition > MIN_POSITION) 
            {
                gripPosition = gripPosition - .01;
            }
            

            
            //set power to drivetrain motors with range of -1 to 1
            fldrive.setPower(Range.clip(fl, -1.0, 1.0));
            frdrive.setPower(Range.clip(fr, -1.0, 1.0));
            brdrive.setPower(Range.clip(br, -1.0, 1.0));
            bldrive.setPower(Range.clip(bl, -1.0, 1.0));

            //set power and direction for other motors
            shooter.setPower(Range.clip(s, -1.0, 1.0));
            
            gripper.setPosition(Range.clip(gripPosition, MIN_POSITION, MAX_POSITION));
            
            
            //debug messages for each motor
            telemetry.addData("fldrive",Double.toString(fldrive.getPower()));
            telemetry.addData("frdrive",Double.toString(frdrive.getPower()));
            telemetry.addData("brdrive",Double.toString(brdrive.getPower()));
            telemetry.addData("bldrive",Double.toString(bldrive.getPower()));

            telemetry.addData("slides",Double.toString(slides.getPower()));
            telemetry.addData("arm",Double.toString(arm.getPower()));
            
            telemetry.addData("gripper", "position=" + gripPosition + "    actual=" + gripper.getPosition());

            telemetry.update();
        }
    }
}
