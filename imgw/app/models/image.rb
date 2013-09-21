
class Image < ActiveRecord::Base
  #define accessible attributes
  attr_accessible :path, :image_name, :tag, :user, :lat, :lng, :tag_list, :hash_sha1 

  #add acts_as_taggable support for this model
  acts_as_taggable

  
  def self.in_range(latp, lngp)
    #find all images
		@tags = Image.find(:all)

    #set some temporary arrays
		@near_tags = []
    @nt = []

    #for each image
		@tags.each do |tag|

      #check if distance is smaller than 50 (m)
	    if distance([latp, lngp],[(tag[:lat].to_f).round(7) , (tag[:lng].to_f).round(7)]) < 50

        #check if there is already the same tag in array
        if !@near_tags.include? tag[:tag]

          #add to arrays
          @near_tags << tag[:tag]
          @nt << tag
        end

	    end

		end

    #return nearest tags
	  return @nt
  end

  #calculate the distance between two point using haversine formula
  def self.distance(a, b)
    #calculate degree -> radian conversion rate
    rad_per_deg = Math::PI/180  
    #set Earth radius in km
    rkm = 6371             
    #set Earth radius in m
    rm = rkm * 1000       

    #calculate distance between corresponding coordinate points in radians
    dlon_rad = (b[1]-a[1]) * rad_per_deg 
    dlat_rad = (b[0]-a[0]) * rad_per_deg

    #get lattitude and longitude in radians
    lat1_rad, lon1_rad = a.map! {|i| i * rad_per_deg }
    lat2_rad, lon2_rad = b.map! {|i| i * rad_per_deg }

    #calculate the distance
    a = Math.sin(dlat_rad/2)**2 + Math.cos(lat1_rad) * Math.cos(lat2_rad) * Math.sin(dlon_rad/2)**2
    c = 2 * Math.asin(Math.sqrt(a))

    #return distance in meters
    return rm * c 
  end
  
end
