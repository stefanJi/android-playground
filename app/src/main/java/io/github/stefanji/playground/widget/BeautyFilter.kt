package io.github.stefanji.playground.widget

import android.opengl.GLES20
import android.util.Log
import com.otaliastudios.cameraview.filter.BaseFilter
import com.otaliastudios.cameraview.internal.GlUtils

/**
 * Create by jy on 2019-10-16
 */
class BeautyFilter(private val factor: Float = 0.9f) : BaseFilter() {

    private var mSizeLocation = -1
    private var mWidth = 0
    private var mHeight = 0
    private var mFactorLocation = -1

    companion object {
        private const val SIZE_LOCATION = "beauty_filter_size"
        private const val FACTOR_LOCATION = "beauty_filter_factor"
    }

    override fun setSize(width: Int, height: Int) {
        super.setSize(width, height)
        mWidth = width
        mHeight = height
        GLES20.glUniform2iv(mSizeLocation, 1, intArrayOf(mWidth, mHeight), 0)
    }

    override fun onCreate(programHandle: Int) {
        super.onCreate(programHandle)
        mSizeLocation = GLES20.glGetUniformLocation(programHandle,
            SIZE_LOCATION
        )
        mFactorLocation = GLES20.glGetUniformLocation(programHandle,
            FACTOR_LOCATION
        )
    }

    override fun onPreDraw(timestampUs: Long, transformMatrix: FloatArray?) {
        super.onPreDraw(timestampUs, transformMatrix)
        val sizes = intArrayOf(mWidth, mHeight)
        Log.d("JY", "size: $mWidth x $mHeight")
        GLES20.glUniform2iv(mSizeLocation, 1, sizes, 0)
        GlUtils.checkError("glUniform2iv")

        GLES20.glUniform1f(mFactorLocation, 0.5f)
        GlUtils.checkError("glUniform1f")
    }

    override fun onDestroy() {
        super.onDestroy()
        mSizeLocation = -1
    }

    override fun getFragmentShader(): String {
        return "#extension GL_OES_EGL_image_external : require\n" +
                "precision lowp float;\n" +
                "uniform ivec2 " + SIZE_LOCATION + ";\n" +
                "uniform samplerExternalOES sTexture;\n" +
                "varying vec2 " + DEFAULT_FRAGMENT_TEXTURE_COORDINATE_NAME + ";\n" +
                "\n" +
                "uniform float " + FACTOR_LOCATION + ";\n" +
                "\n" +
                "void main() {\n" +
                "vec2 vTextureCoord = " + DEFAULT_FRAGMENT_TEXTURE_COORDINATE_NAME + ";\n" +
                "    vec3 centralColor;\n" +
                "\n" +
                "    centralColor = texture2D(sTexture, vTextureCoord).rgb;\n" +
                "\n" +
                "    if(" + FACTOR_LOCATION + " < 0.01) {\n" +
                "        gl_FragColor = vec4(centralColor, 1.0);\n" +
                "    } else {\n" +
                "        float x_a = float(" + SIZE_LOCATION + "[0]);\n" +
                "        float y_a = float(" + SIZE_LOCATION + "[1]);\n" +
                "\n" +
                "        float mul_x = 2.0 / x_a;\n" +
                "        float mul_y = 2.0 / y_a;\n" +
                "        vec2 blurCoordinates0 = vTextureCoord + vec2(0.0 * mul_x, -10.0 * mul_y);\n" +
                "        vec2 blurCoordinates2 = vTextureCoord + vec2(8.0 * mul_x, -5.0 * mul_y);\n" +
                "        vec2 blurCoordinates4 = vTextureCoord + vec2(8.0 * mul_x, 5.0 * mul_y);\n" +
                "        vec2 blurCoordinates6 = vTextureCoord + vec2(0.0 * mul_x, 10.0 * mul_y);\n" +
                "        vec2 blurCoordinates8 = vTextureCoord + vec2(-8.0 * mul_x, 5.0 * mul_y);\n" +
                "        vec2 blurCoordinates10 = vTextureCoord + vec2(-8.0 * mul_x, -5.0 * mul_y);\n" +
                "\n" +
                "        mul_x = 1.8 / x_a;\n" +
                "        mul_y = 1.8 / y_a;\n" +
                "        vec2 blurCoordinates1 = vTextureCoord + vec2(5.0 * mul_x, -8.0 * mul_y);\n" +
                "        vec2 blurCoordinates3 = vTextureCoord + vec2(10.0 * mul_x, 0.0 * mul_y);\n" +
                "        vec2 blurCoordinates5 = vTextureCoord + vec2(5.0 * mul_x, 8.0 * mul_y);\n" +
                "        vec2 blurCoordinates7 = vTextureCoord + vec2(-5.0 * mul_x, 8.0 * mul_y);\n" +
                "        vec2 blurCoordinates9 = vTextureCoord + vec2(-10.0 * mul_x, 0.0 * mul_y);\n" +
                "        vec2 blurCoordinates11 = vTextureCoord + vec2(-5.0 * mul_x, -8.0 * mul_y);\n" +
                "\n" +
                "        mul_x = 1.6 / x_a;\n" +
                "        mul_y = 1.6 / y_a;\n" +
                "        vec2 blurCoordinates12 = vTextureCoord + vec2(0.0 * mul_x,-6.0 * mul_y);\n" +
                "        vec2 blurCoordinates14 = vTextureCoord + vec2(-6.0 * mul_x,0.0 * mul_y);\n" +
                "        vec2 blurCoordinates16 = vTextureCoord + vec2(0.0 * mul_x,6.0 * mul_y);\n" +
                "        vec2 blurCoordinates18 = vTextureCoord + vec2(6.0 * mul_x,0.0 * mul_y);\n" +
                "\n" +
                "        mul_x = 1.4 / x_a;\n" +
                "        mul_y = 1.4 / y_a;\n" +
                "        vec2 blurCoordinates13 = vTextureCoord + vec2(-4.0 * mul_x,-4.0 * mul_y);\n" +
                "        vec2 blurCoordinates15 = vTextureCoord + vec2(-4.0 * mul_x,4.0 * mul_y);\n" +
                "        vec2 blurCoordinates17 = vTextureCoord + vec2(4.0 * mul_x,4.0 * mul_y);\n" +
                "        vec2 blurCoordinates19 = vTextureCoord + vec2(4.0 * mul_x,-4.0 * mul_y);\n" +
                "\n" +
                "        float central;\n" +
                "        float gaussianWeightTotal;\n" +
                "        float sum;\n" +
                "        float sampler;\n" +
                "        float distanceFromCentralColor;\n" +
                "        float gaussianWeight;\n" +
                "\n" +
                "        float distanceNormalizationFactor = 3.6;\n" +
                "\n" +
                "        central = texture2D(sTexture, vTextureCoord).g;\n" +
                "        gaussianWeightTotal = 0.2;\n" +
                "        sum = central * 0.2;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates0).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates1).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates2).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates3).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates4).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates5).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates6).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates7).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates8).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates9).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates10).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates11).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.09 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates12).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates13).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates14).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates15).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates16).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates17).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates18).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sampler = texture2D(sTexture, blurCoordinates19).g;\n" +
                "        distanceFromCentralColor = min(abs(central - sampler) * distanceNormalizationFactor, 1.0);\n" +
                "        gaussianWeight = 0.1 * (1.0 - distanceFromCentralColor);\n" +
                "        gaussianWeightTotal += gaussianWeight;\n" +
                "        sum += sampler * gaussianWeight;\n" +
                "\n" +
                "        sum = sum/gaussianWeightTotal;\n" +
                "\n" +
                "        sampler = centralColor.g - sum + 0.5;\n" +
                "\n" +
                "        // 高反差保留\n" +
                "        for(int i = 0; i < 5; ++i) {\n" +
                "            if(sampler <= 0.5) {\n" +
                "                sampler = sampler * sampler * 2.0;\n" +
                "            } else {\n" +
                "                sampler = 1.0 - ((1.0 - sampler)*(1.0 - sampler) * 2.0);\n" +
                "            }\n" +
                "        }\n" +
                "\n" +
                "        float aa = 1.0 + pow(sum, 0.3) * 0.09;\n" +
                "        vec3 smoothColor = centralColor * aa - vec3(sampler) * (aa - 1.0);\n" +
                "        smoothColor = clamp(smoothColor, vec3(0.0), vec3(1.0));\n" +
                "\n" +
                "        smoothColor = mix(centralColor, smoothColor, pow(centralColor.g, 0.33));\n" +
                "        smoothColor = mix(centralColor, smoothColor, pow(centralColor.g, 0.39));\n" +
                "\n" +
                "        smoothColor = mix(centralColor, smoothColor, " + FACTOR_LOCATION + ");\n" +
                "\n" +
                "        gl_FragColor = vec4(pow(smoothColor, vec3(0.96)), 1.0);\n" +
                "    }\n" +
                " }"
    }
}