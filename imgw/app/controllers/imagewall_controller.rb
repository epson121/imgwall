require 'fileutils'
require 'digest/sha1'

class ImagewallController < ApplicationController

	#define response extensions
	respond_to :html, :json

	def index
	  @images = Image.find(:all)
	
	  #if there are GPS coordinates in params, calculate nearest TAGs
	  if params[:lng] != nil and params[:lat] != nil
	  	#convert parameters to floats
	  	fl_lat = Float(params[:lat])
	  	fl_lng = Float(params[:lng])
	  	#find all TAGs that are in range
	    @image = Image.in_range(fl_lat, fl_lng)
	  end	

	  #If there is a TAG in params
	  if params[:tag]
	  	#get all images tagged with that tag
   	    @images = Image.tagged_with(params[:tag])
 	  else
 	  	#get all images
     	@images = Image.find(:all)
  	  end
	  #return TAG list --> for JSON GET request
      respond_with @image
    
	end

	def create
	  #get POST parameters
	  images = Image.find(:all)
	  tag = params[:tag]
	  user = params[:user]
	  image_string = params[:img]
	  lat = params[:lat]
	  lng = params[:lng]

	  @return_value = 1
	  
	  #generate random value that represents image name
	  uuid = SecureRandom.hex

	  #setup a path to image folder and create one if it doesn't exist
	  path = 'app/assets/images'
	  FileUtils.mkdir_p(path) unless File.exists?(path)

	  
	  #save image in the folder if there are no same images
	  File.open(path + "/" + uuid + ".jpg", 'wb') do |f|
	  	#decode image from base64
	    file = Base64.decode64(image_string)

	    #calculate SHA1 digest
	    hash = Digest::SHA1.hexdigest file

	    #check if there are images with the same digest
	    images.each do |img|
	    	if img[:hash_sha1] == hash
	    		@return_value = 0
	    	end
	    end

	    #if picture is unique
	    if @return_value == 1
	    	#write it to file
	    	f.write(f.write(Base64.decode64(image_string)))
	    	path = path + "/" + uuid + '.jpg'

	    	#add entry to database
	  		entry = Image.create(:user => user,:path => path, :image_name => uuid + ".jpg", :tag_list => tag, :lat => lat, :lng => lng, :tag => tag, :hash_sha1 => hash)
	    end
	  end

	  #render a return value for POST request
	  render :text => @return_value
	end

end
