# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)
'''
Image.create(:user => "luka1",:path => "path/to/image", :tag_list => "tag1", :lat => "lat", :lng => "lng", :tag => "tag1")
Image.create(:user => "luka2",:path => "path/to/image", :tag_list => "tag2", :lat => "lat", :lng => "lng", :tag => "tag2")
Image.create(:user => "luka3",:path => "path/to/image", :tag_list => "tag3", :lat => "lat", :lng => "lng", :tag => "tag3")
Image.create(:user => "luka1",:path => "path/to/image", :tag_list => "tag1", :lat => "lat", :lng => "lng", :tag => "tag1")
Image.create(:user => "luka1",:path => "path/to/image", :tag_list => "tag1", :lat => "lat", :lng => "lng", :tag => "tag1")
Image.create(:user => "luka1",:path => "path/to/image", :tag_list => "tag1", :lat => "lat", :lng => "lng", :tag => "tag1")
Image.create(:user => "luka3",:path => "path/to/image", :tag_list => "tag3", :lat => "lat", :lng => "lng", :tag => "tag3")
'''