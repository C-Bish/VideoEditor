SOFTENG 751 GROUP 23, Parallel Video Editing Application

How to use libraries and APIs we have used in our implementation

  Installing JavaCV: https://github.com/bytedeco/javacv
  Use JavaCV 1.3.2, download the bin zip file and include the .jar files in your project build path.

  Using ffmpeg:
  Download the static build for the corresponding system from: https://ffmpeg.org/download.html
  Place the ffmpeg.exe from the bin library and include in filepath.
  
  Using JavaFX:
  Install JavaFX through the following link
  http://www.eclipse.org/efxclipse/install.html
  
IMPORTANT NOTES:
1) Preview for some filters (invert colours and mirror) has been disabled as displaying the filtered image was sometimes causing memory issues. Despite this fact you can still filter the video using these filters.
2) Some filter buttons have not been assigned a filter label (they do not do anything). This is due to the JavaCV filtering implementation limiting us to only being able to use some filters. (More information about this in the report). These filters include:
  * Pixelise
  * Thresholding
  * Halftone
  * Edge Detector
  * Difference
3) At the moment the parallel implementation output video does not have audio mapped to it. This is unexplained as we perform the FFmpeg commmand to map the audio but it does not work in this case.
4) When using the parallel implementation the progress bar does not increment gradually. (Did not get time to fix this).

