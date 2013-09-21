class AddHashToDb < ActiveRecord::Migration
  def change
  	change_table :images do |t|
		t.string :hash
  	end
  end
end
