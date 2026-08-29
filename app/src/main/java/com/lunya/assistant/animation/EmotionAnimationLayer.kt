package com.lunya.assistant.animation

/**
 * Layer that applies emotion-driven face + body language.
 */
class EmotionAnimationLayer : AnimationLayer {
    override val name = "emotion"
    override var weight = 1f
    override var enabled = true

    var emotion: Emotion = Emotion.IDLE
    var blendSpeed = 6f

    private var current = Emotion.IDLE

    override fun evaluate(timeSec: Float, pose: MutableMap<BodyPart, PartTransform>) {
        if (current != emotion) current = emotion

        val lib = ProceduralMotionLibrary
        when (current) {
            Emotion.HAPPY, Emotion.EXCITED -> {
                pose[BodyPart.HEAD] = lib.happyBounce(timeSec)
                pose[BodyPart.EYES] = PartTransform(scaleY = 0.7f)
                pose[BodyPart.MOUTH] = PartTransform(scaleY = 1.3f, scaleX = 1.2f)
                pose[BodyPart.TAIL] = lib.tailWag(timeSec, 0.9f)
            }
            Emotion.TSUNDERE -> {
                pose[BodyPart.HEAD] = lib.tsundereLookAway(timeSec)
                pose[BodyPart.EYES] = PartTransform(offsetY = -2f, scaleY = 0.85f)
                pose[BodyPart.ARM_LEFT] = PartTransform(rotation = -25f)
            }
            Emotion.SHY -> {
                pose[BodyPart.HEAD] = PartTransform(offsetY = 4f, rotation = 8f)
                pose[BodyPart.ARM_LEFT] = PartTransform(offsetY = -8f, rotation = 30f)
                pose[BodyPart.ARM_RIGHT] = PartTransform(offsetY = -8f, rotation = -30f)
            }
            Emotion.SLEEPY -> {
                pose[BodyPart.HEAD] = lib.sleepSway(timeSec)
                pose[BodyPart.EYELIDS] = PartTransform(scaleY = 0.15f)
                pose[BodyPart.TORSO] = lib.breathe(timeSec, 0.4f)
            }
            Emotion.SURPRISED -> {
                pose[BodyPart.HEAD] = PartTransform(offsetY = -6f, scaleY = 1.08f)
                pose[BodyPart.EYES] = PartTransform(scaleX = 1.25f, scaleY = 1.3f)
                pose[BodyPart.MOUTH] = PartTransform(scaleY = 1.4f)
                pose[BodyPart.EARS_LEFT] = PartTransform(rotation = -20f)
                pose[BodyPart.EARS_RIGHT] = PartTransform(rotation = 20f)
            }
            Emotion.ANGRY -> {
                pose[BodyPart.HEAD] = PartTransform(offsetY = 2f)
                pose[BodyPart.EYES] = PartTransform(scaleY = 0.55f, offsetY = 2f)
                pose[BodyPart.MOUTH] = PartTransform(scaleY = 0.6f, scaleX = 0.9f)
            }
            Emotion.OVERCLOCKED -> {
                pose[BodyPart.HEAD] = lib.overclockShake(timeSec)
                pose[BodyPart.AURA] = PartTransform(scaleX = 1.2f, scaleY = 1.2f, alpha = 0.9f)
                pose[BodyPart.TORSO] = lib.breathe(timeSec, 1.5f)
            }
            Emotion.LOVE -> {
                pose[BodyPart.HEAD] = lib.headBob(timeSec, current)
                pose[BodyPart.EYES] = PartTransform(scaleY = 0.75f)
                pose[BodyPart.TAIL] = lib.tailWag(timeSec, 0.7f)
            }
            Emotion.CURIOUS -> {
                pose[BodyPart.HEAD] = PartTransform(
                    rotation = ProceduralMotionLibrary.sine(timeSec, 0.3f, 10f),
                    offsetY = -3f
                )
                pose[BodyPart.EARS_LEFT] = PartTransform(rotation = -12f)
                pose[BodyPart.EARS_RIGHT] = PartTransform(rotation = 15f)
            }
            else -> {
                pose[BodyPart.HEAD] = lib.headBob(timeSec, current)
                pose[BodyPart.TORSO] = lib.breathe(timeSec, 0.8f)
            }
        }

        if (current != Emotion.SLEEPY) {
            val blink = lib.eyeBlink(timeSec)
            val eye = pose.getOrPut(BodyPart.EYELIDS) { PartTransform() }
            eye.scaleY = blink
        }
    }
}
