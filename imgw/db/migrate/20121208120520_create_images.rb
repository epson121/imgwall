class CreateImages < ActiveRecord::Migration
  def change
    create_table :images do |t|
      t.string :user
      t.string :path
      t.string :tag

      t.timestamps
    end
  end
end
